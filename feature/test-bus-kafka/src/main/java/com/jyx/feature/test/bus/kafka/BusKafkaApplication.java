package com.jyx.feature.test.bus.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author jiangyaxin
 */
@RemoteApplicationEventScan
@SpringBootApplication
public class BusKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusKafkaApplication.class, args);
    }

}
