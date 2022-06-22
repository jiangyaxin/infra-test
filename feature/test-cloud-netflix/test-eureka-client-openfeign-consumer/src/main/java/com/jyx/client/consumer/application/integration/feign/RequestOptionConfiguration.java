package com.jyx.client.consumer.application.integration.feign;

import feign.Request;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangyaxin
 * @since 2022/5/18 21:09
 */
public class RequestOptionConfiguration {

    @Bean
    public Request.Options options() {
        return new Request.Options(
                // 连接超时配置
                5, TimeUnit.SECONDS,
                // 读超时配置
                4, TimeUnit.SECONDS,
                // 如果请求响应3xx，是否重定向请求
                false);
    }
}
