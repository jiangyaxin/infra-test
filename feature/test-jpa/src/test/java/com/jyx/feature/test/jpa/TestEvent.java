package com.jyx.feature.test.jpa;

import org.springframework.context.ApplicationEvent;

/**
 * @author JYX
 * @since 2021/11/9 8:53
 */
public class TestEvent extends ApplicationEvent {

    public TestEvent(Object source) {
        super(source);
    }
}
