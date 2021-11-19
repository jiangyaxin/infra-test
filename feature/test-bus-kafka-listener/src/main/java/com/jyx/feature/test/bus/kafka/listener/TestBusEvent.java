package com.jyx.feature.test.bus.kafka.listener;

import com.jyx.infra.bus.event.GlobalApplicationEvent;

/**
 * @author JYX
 * @since 2021/11/18 10:56
 */
public class TestBusEvent extends GlobalApplicationEvent {

    /**
     * 该构造器不能使用，保留是为了序列化使用,否则出现 java.lang.NullPointerException ServiceMatcher
     */
    public TestBusEvent() {
    }

    public TestBusEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
    }

    public TestBusEvent(Object source, String originService) {
        super(source, originService);
    }
}
