package com.jyx.feature.test.disruptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author JYX
 * @since 2021/10/13 16:00
 */
@SpringBootApplication
@ComponentScan("com.jyx")
public class DisruptorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisruptorApplication.class, args);
    }

}

