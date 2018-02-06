package com.gs.cache.config;

import com.gs.cache.CacheProxy;
import com.gs.cache.impl.LocalCacheProxyImpl;
import com.gs.cache.impl.RedisCacheProxyImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by linjuntan on 2017/10/29.
 * email: ljt1343@gmail.com
 */
@Configuration
@Slf4j
public class CacheConfiguration {
    @Value("${redis.maxActive:16}")
    private int maxActive;
    @Value("${redis.maxIdle:16}")
    private int maxIdle;
    @Value("${redis.minIdle:8}")
    private int minIdle;
    @Value("${redis.maxWait:1000}")
    private int maxWait;
    @Value("${redis.lifo:false}")
    private boolean lifo;
    @Value("${redis.testOnBorrow:false}")
    private boolean testOnBorrow;
    @Value("${redis.shared.url:redis://@127.0.0.1:6579/0}")
    private String jedisSharedInfo;

    @Bean("jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setLifo(lifo);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        return jedisPoolConfig;
    }

    @Bean("shardedJedisPool")
    @DependsOn("jedisPoolConfig")
    public ShardedJedisPool shardedJedisPool(@Autowired JedisPoolConfig jedisPoolConfig) {
        try {
            ShardedJedisPool jedisPool = new ShardedJedisPool(jedisPoolConfig,
                    Arrays.stream(jedisSharedInfo.split(",")).map(JedisShardInfo::new).collect(Collectors.toList()));
            return jedisPool;
        } catch (Exception e) {
            log.error(" >>> init redis pool error", e);
            log.error(" >>> will be use local cache");
        }

        return null;
    }

    @Bean
    @DependsOn("shardedJedisPool")
    public CacheProxy cacheProxy(@Autowired ShardedJedisPool shardedJedisPool) {
        if (shardedJedisPool == null) {
            log.info(" >>> create local cache");
            return new LocalCacheProxyImpl();
        }

        log.info(" >>> create redis cache");
        RedisCacheProxyImpl cacheProxy = new RedisCacheProxyImpl();
        cacheProxy.setShardedJedisPool(shardedJedisPool);

        return cacheProxy;
    }
}
