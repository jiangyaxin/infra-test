package com.jyx.feature.test.bus.kafka;

import com.jyx.infra.bus.event.GlobalApplicationEvent;

/**
 * @author JYX
 * @since 2021/11/18 10:56
 */
public class TestBusEvent extends GlobalApplicationEvent {

    public TestBusEvent() {
    }

    public TestBusEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
    }

    public TestBusEvent(Object source, String originService) {
        super(source, originService);
    }
}
