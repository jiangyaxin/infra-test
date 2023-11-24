package com.jyx.feature.test.bus.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2021/11/19 10:45
 */
@Slf4j
@Component
public class TestBusListener {

    @EventListener
    public void testBusEvent(TestBusEvent event){
        log.info("Event is {}.",event);
    }
}
