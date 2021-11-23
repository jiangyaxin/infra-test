package com.jyx.infra.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author JYX
 * @since 2021/11/22 15:52
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService{

    private final RedisTemplate<String, Object> redisTemplate;

    private final ValueOperations<String, Object> valueOperations;

    private final HashOperations<String,String,Object> hashOperations;

    private final ListOperations<String, Object> listOperations;

    private final SetOperations<String, Object> setOperations;

    @Override
    public void set(String key, Object value, long time) {
        valueOperations.set(key,value,time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        valueOperations.set(key, value);
    }

    @Override
    public Object get(String key) {
        return valueOperations.get(key);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long delete(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long increase(String key, long delta) {
        return valueOperations.increment(key, delta);
    }

    @Override
    public Long decrease(String key, long delta) {
        return valueOperations.increment(key, -delta);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return hashOperations.get(key,hashKey) ;
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        hashOperations.put(key,hashKey,value);
        return expire(key,time);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        hashOperations.put(key,hashKey,value);
    }

    @Override
    public Map<String, Object> hGetAll(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        hashOperations.putAll(key, map);
        return expire(key, time);
    }

    @Override
    public void hSetAll(String key, Map<String, Object> map) {
        hashOperations.putAll(key, map);
    }

    @Override
    public void hDel(String key, Object... hashKey) {
        hashOperations.delete(key, hashKey);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return hashOperations.hasKey(key, hashKey);
    }

    @Override
    public Long hIncrease(String key, String hashKey, Long delta) {
        return hashOperations.increment(key, hashKey, delta);
    }

    @Override
    public Long hDecrease(String key, String hashKey, Long delta) {
        return hashOperations.increment(key, hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return setOperations.members(key);
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return setOperations.add(key, values);
    }

    @Override
    public Long sAdd(String key, long time, Object... values) {
        Long count = setOperations.add(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return setOperations.isMember(key, value);
    }

    @Override
    public Long sSize(String key) {
        return setOperations.size(key);
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return setOperations.remove(key, values);
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        return listOperations.range(key, start, end);
    }

    @Override
    public Long lSize(String key) {
        return listOperations.size(key);
    }

    @Override
    public Object lIndex(String key, long index) {
        return listOperations.index(key, index);
    }

    @Override
    public Long lPush(String key, Object value) {
        return listOperations.rightPush(key, value);
    }

    @Override
    public Long lPush(String key, Object value, long time) {
        Long index = listOperations.rightPush(key, value);
        expire(key, time);
        return index;
    }

    @Override
    public Long lPushAll(String key, Object... values) {
        return listOperations.rightPushAll(key, values);
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        Long count = listOperations.rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Long lRemove(String key, long count, Object value) {
        return listOperations.remove(key, count, value);
    }
}
