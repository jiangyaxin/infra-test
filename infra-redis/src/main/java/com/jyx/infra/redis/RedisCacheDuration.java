package com.jyx.infra.redis;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/**
 * @author jiangyaxin
 * @since 2021/11/23 9:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheDuration {

    private Map<String, Duration> cacheDuration = Maps.newHashMap();
}
