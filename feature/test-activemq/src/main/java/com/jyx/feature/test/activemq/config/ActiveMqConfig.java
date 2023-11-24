package com.jyx.feature.test.activemq.config;

import org.springframework.context.annotation.Bean;

import javax.jms.ConnectionFactory;

/**
 * @author jiangyaxin
 * @since 2023/5/11 11:26
 */
public class ActiveMqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return null;
    }
}
