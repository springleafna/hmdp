package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryType() {
        String key = CACHE_SHOP_TYPE_KEY;
        //1.从redis中查询店铺类型缓存
        List<String> shopTypeList = stringRedisTemplate.opsForList().range(key, 0, -1);

        //2.判断是否存在
        if (shopTypeList != null && !shopTypeList.isEmpty()) {
            //3.存在，直接返回
            //转换成ShopType类型的list
            List<ShopType> typeList = new ArrayList<>();
            for (String s : shopTypeList) {
                ShopType shopType = JSONUtil.toBean(s, ShopType.class);
                typeList.add(shopType);
            }
            return Result.ok(typeList);
        }

        //4.不存在，获取数据库中的商铺类型数据
        List<ShopType> typeList = query().orderByAsc("sort").list();
        //5.数据库中不存在，返回错误
        if (typeList == null || typeList.isEmpty()) {
            return Result.fail("无店铺类型！");
        }
        //6.数据库中存在，先写入redis
        for (ShopType shopType : typeList) {
            shopTypeList.add(JSONUtil.toJsonStr(shopType));
        }
        stringRedisTemplate.opsForList().rightPushAll(key, shopTypeList);
        //7、返回结果
        return Result.ok(typeList);
    }
}
