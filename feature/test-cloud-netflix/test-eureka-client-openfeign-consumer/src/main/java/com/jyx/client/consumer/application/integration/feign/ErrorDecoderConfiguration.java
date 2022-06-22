package com.jyx.client.consumer.application.integration.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @author jiangyaxin
 * @since 2022/5/19 22:29
 */
public class ErrorDecoderConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder(objectMapper);
    }
}
