package com.jyx.infra.redis;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jiangyaxin
 * @since 2021/11/23 8:17
 */
@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(RedisTemplate<String, Object> redisTemplate, CacheProperties cacheProperties) {
        //spring.cache.redis
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        // 缓存使用和redis一样的序列化方式
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));

        // 全局 TTL 时间
        Duration timeToLive = redisProperties.getTimeToLive();
        // key 前缀值
        String keyPrefix = redisProperties.getKeyPrefix();
        // 默认为ture,缓存null值 可以防止缓存穿透
        boolean cacheNullValues = redisProperties.isCacheNullValues();
        // 默认为true
        boolean useKeyPrefix = redisProperties.isUseKeyPrefix();


        if (Objects.nonNull(timeToLive)) {
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(timeToLive);
        }
        if (Objects.nonNull(keyPrefix)) {
            redisCacheConfiguration = redisCacheConfiguration.prefixCacheNameWith(keyPrefix);
        }
        if (!cacheNullValues) {
            redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
        }
        if (!useKeyPrefix) {
            redisCacheConfiguration = redisCacheConfiguration.disableKeyPrefix();
        }
        return redisCacheConfiguration;
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(RedisCacheConfiguration redisCacheConfiguration, RedisCacheDuration redisCacheDuration) {
        return redisCacheManagerBuilder ->
                redisCacheManagerBuilder.cacheDefaults(redisCacheConfiguration)
                        .withInitialCacheConfigurations(redisCacheDuration.getCacheDuration().entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        entry -> redisCacheConfiguration.entryTtl(entry.getValue()))));
    }

}
