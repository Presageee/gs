package com.gs.cache.impl;

import com.gs.cache.CacheProxy;
import com.gs.cache.CacheType;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author: linjuntan
 * date: 2018/2/6
 */
@Slf4j
public class LocalCacheProxyImpl implements CacheProxy {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String OK = "OK!";

    private static final Long OK_LONG = 1L;

    private static final Long NO_EXPIRED = -1L;

    private Lock lock = new ReentrantLock();

    private ConcurrentHashMap<String, LocalCacheData> cacheMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public LocalCacheProxyImpl() {
        lock = new ReentrantLock();
        cacheMap = new ConcurrentHashMap<>();

        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1, new CleanCacheThreadFactory("localCache"));
        service.scheduleWithFixedDelay(this::cleanMap, 0, 60, TimeUnit.SECONDS);
    }

    @Override
    public CacheType getCacheType() {
        return CacheType.LOCAL;
    }

    @Override
    public String set(String key, String value) {
        LocalCacheData data = new LocalCacheData(value.getBytes(UTF8));
        cacheMap.put(key, data);
        return OK;
    }

    @Override
    public String get(String key) {
        LocalCacheData data = cacheMap.get(key);
        if (data == null) {
            return null;
        }

        return new String(data.getData(), UTF8);
    }

    @Override
    public Boolean exists(String key) {
        return cacheMap.containsKey(key);
    }



    @Override
    public Long expire(String key, int seconds) {
        LocalCacheData data = cacheMap.get(key);
        if (data == null) {
            return null;
        }

        data.setExpiredTime(seconds * 1000);
        data.setCreateTime(System.currentTimeMillis());
        return OK_LONG;
    }

    @Override
    public Long del(String key) {
        if ( delKey(key) == null) {
            return null;
        }
        return OK_LONG;
    }

    @Override
    public Long ttl(String key) {
        LocalCacheData data = cacheMap.get(key);
        if (data == null) {
            return null;
        }

        if (data.getExpiredTime() == NO_EXPIRED) {
            return NO_EXPIRED;
        }

        long dist = System.currentTimeMillis() - data.getCreateTime();

        long ttl = data.getExpiredTime() - dist;
        if (ttl < 0) {
            //remove key
            delKey(key);
            return null;
        }

        return ttl / 1000;
    }

    private Long delKey(String key) {
        try {
            lock.lock();
            cacheMap.remove(key);
            return OK_LONG;
        } catch (Exception e) {
            log.error(" >>> delete key error,", e);
        } finally {
            lock.unlock();
        }

        return null;
    }

    private void cleanMap() {
        try {
            lock.lock();
            Iterator<Map.Entry<String, LocalCacheData>> iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, LocalCacheData> entry = iterator.next();
                LocalCacheData data = entry.getValue();

                long dist = System.currentTimeMillis() - data.getCreateTime();
                long ttl = data.getExpiredTime() - dist;

                if (ttl <= 0) {
                    cacheMap.remove(entry.getKey());
                }
            }
        } finally {
            lock.unlock();
        }
    }




    @Override
    public String type(String key) {
        return null;
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        return null;
    }

    @Override
    public String setex(String key, int seconds, String value) {
        LocalCacheData data = new LocalCacheData(seconds * 1000, value.getBytes(UTF8));
        cacheMap.put(key, data);
        return OK;
    }

    @Override
    public Long decrBy(String key, long integer) {
        return null;
    }

    @Override
    public long setrange(String key, long offset, String value) {
        return 0;
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        return null;
    }

    @Override
    public String getSet(String key, String value) {
        return null;
    }

    @Override
    public Long setnx(String key, String value) {
        return null;
    }

    @Override
    public Long decr(String key) {
        return null;
    }

    @Override
    public Long incrBy(String key, long integer) {
        return null;
    }

    @Override
    public Long incr(String key) {
        return null;
    }

    @Override
    public Long append(String key, String value) {
        return null;
    }

    @Override
    public String substr(String key, int start, int end) {
        return null;
    }

    @Override
    public Long hset(String key, String field, String value) {
        return null;
    }

    @Override
    public String hget(String key, String field) {
        return null;
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        return null;
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        return null;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        return null;
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        return null;
    }

    @Override
    public Boolean hexists(String key, String field) {
        return null;
    }

    @Override
    public Long hdel(String key, String field) {
        return null;
    }

    @Override
    public Long hlen(String key) {
        return null;
    }

    @Override
    public Set<String> hkeys(String key) {
        return null;
    }

    @Override
    public List<String> hvals(String key) {
        return null;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return null;
    }

    @Override
    public Long rpush(String key, String string) {
        return null;
    }

    @Override
    public Long lpush(String key, String string) {
        return null;
    }

    @Override
    public Long llen(String key) {
        return null;
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        return null;
    }

    @Override
    public String ltrim(String key, long start, long end) {
        return null;
    }

    @Override
    public String lindex(String key, long index) {
        return null;
    }

    @Override
    public String lset(String key, long index, String value) {
        return null;
    }

    @Override
    public Long lrem(String key, long count, String value) {
        return null;
    }

    @Override
    public String lpop(String key) {
        return null;
    }

    @Override
    public String rpop(String key) {
        return null;
    }

    @Override
    public Long sadd(String key, String member) {
        return null;
    }

    @Override
    public Set<String> smembers(String key) {
        return null;
    }

    @Override
    public Long srem(String key, String member) {
        return null;
    }

    @Override
    public String spop(String key) {
        return null;
    }

    @Override
    public Long scard(String key) {
        return null;
    }

    @Override
    public Boolean sismember(String key, String member) {
        return null;
    }

    @Override
    public String srandmember(String key) {
        return null;
    }

    @Override
    public Long zadd(String key, double score, String member) {
        return null;
    }

    @Override
    public Set<String> zrange(String key, int start, int end) {
        return null;
    }

    @Override
    public Long zrem(String key, String member) {
        return null;
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        return null;
    }

    @Override
    public Long zrank(String key, String member) {
        return null;
    }

    @Override
    public Long zrevrank(String key, String member) {
        return null;
    }

    @Override
    public Set<String> zrevrange(String key, int start, int end) {
        return null;
    }

    @Override
    public Long zcard(String key) {
        return null;
    }

    @Override
    public Double zscore(String key, String member) {
        return null;
    }

    @Override
    public List<String> sort(String key) {
        return null;
    }

    @Override
    public Long zcount(String key, double min, double max) {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return null;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return null;
    }

    @Override
    public Long zremrangeByRank(String key, int start, int end) {
        return null;
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        return null;
    }
}
