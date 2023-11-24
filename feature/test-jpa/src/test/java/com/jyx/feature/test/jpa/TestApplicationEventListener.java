package com.jyx.feature.test.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2021/11/9 8:55
 */
@Slf4j
@Component
public class TestApplicationEventListener {

    @EventListener
    public void testEvent(TestEvent event){
        log.info("收到testEvent={}",event);
    }
}
