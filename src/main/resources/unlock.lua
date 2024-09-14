--�Ƚ��̱߳�ʶ�����еı�ʶ�Ƿ�һ��
if (redis.call('get', KEYS[1] == ARGV[1])) then
    --�ͷ��� del key
    return redis.call('del', KEYS[1])
end
return 0