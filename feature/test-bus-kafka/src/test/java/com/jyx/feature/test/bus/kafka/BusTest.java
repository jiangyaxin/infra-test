package com.jyx.feature.test.bus.kafka;

import com.jyx.feature.test.bus.kafka.listener.TestBusEvent;
import com.jyx.infra.bus.context.BusContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.bus.ServiceMatcher;

/**
 * @author jiangyaxin
 * @since 2021/11/18 10:55
 */
@Slf4j
@SpringBootTest
public class BusTest {

    @Autowired
    private ServiceMatcher serviceMatcher;

    @Test
    public void testBusContext(){
        BusContextHolder.publishGlobalEvent(new TestBusEvent("Test.",serviceMatcher.getBusId()));
    }

    @Test
    public void testBusDestinationContext(){
        BusContextHolder.publishGlobalEvent(new TestBusEvent("Test.",serviceMatcher.getBusId(),"bus-kafka-listener:**"));
    }

    @Test
    public void testBusNotSendKafkaListenerContext(){
        BusContextHolder.publishGlobalEvent(new TestBusEvent("Test.",serviceMatcher.getBusId(),"bus-kafka:**"));
    }

    @Test
    public void testServiceMatcher(){
        log.info("ServiceMatcher id is : {}",serviceMatcher.getBusId());
    }
}
