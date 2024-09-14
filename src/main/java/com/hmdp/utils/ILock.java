package com.hmdp.utils;

public interface ILock {

    /*
     *尝试获取锁
     * @param timeoutSec 所持有的超时时间，过期后自动释放
     * @return true：代表锁获取成功      false：代表锁获取失败
     *  */
    boolean tryLock(long timeoutSec);

    /*
     * 释放锁
     * */
    void unlock();
}
