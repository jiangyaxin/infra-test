package com.jyx.feature.test.bus.kafka.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author asa
 */
@ComponentScan("com.jyx")
@RemoteApplicationEventScan
@SpringBootApplication
public class BusKafkaListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusKafkaListenerApplication.class, args);
    }

}
