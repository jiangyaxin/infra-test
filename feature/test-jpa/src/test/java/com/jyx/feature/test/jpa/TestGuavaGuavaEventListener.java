package com.jyx.feature.test.jpa;

import com.google.common.eventbus.Subscribe;
import com.jyx.infra.spring.event.GuavaEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JYX
 * @since 2021/11/9 15:40
 */
@Slf4j
public class TestGuavaGuavaEventListener extends GuavaEventListener {

    @Subscribe
    public void testEvent(TestGuavaEvent testGuavaEvent){
        log.info("消费Event={}", testGuavaEvent);
    }

}
