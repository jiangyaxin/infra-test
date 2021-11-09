package com.jyx.feature.test.jpa;

import com.google.common.eventbus.Subscribe;
import com.jyx.infra.event.EventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JYX
 * @since 2021/11/9 15:40
 */
@Slf4j
public class GuavaEventListener extends EventListener {

    @Subscribe
    public void testEvent(GuavaEvent guavaEvent){
        log.info("消费Event={}",guavaEvent);
    }

}
