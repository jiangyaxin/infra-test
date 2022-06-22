package com.jyx.client.consumer.application.integration.feign;

import feign.Retryer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangyaxin
 * @since 2022/5/19 18:43
 */
public class RetryConfiguration {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(500,TimeUnit.SECONDS.toMillis(2),5);
    }

}
