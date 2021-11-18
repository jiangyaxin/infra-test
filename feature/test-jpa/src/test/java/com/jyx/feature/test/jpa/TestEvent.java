package com.jyx.feature.test.jpa;

import com.jyx.infra.event.LocalApplicationEvent;

/**
 * @author JYX
 * @since 2021/11/9 8:53
 */
public class TestEvent extends LocalApplicationEvent {

    public TestEvent(Object source) {
        super(source);
    }
}
