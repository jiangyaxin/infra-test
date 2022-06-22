package com.jyx.client.consumer.application.integration.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangyaxin
 * @since 2022/5/18 22:09
 */
@Configuration
public class LogLevelConfiguration {
    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }
}
