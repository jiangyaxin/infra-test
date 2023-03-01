package com.jyx.feature.test.jpa;

import com.jyx.infra.spring.context.SpringContextHolder;
import com.jyx.infra.spring.event.GuavaEventBusContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JYX
 * @since 2021/10/20 22:42
 */
@Slf4j
@SpringBootTest
public class BusTest {

    @Test
    public void testGuavaEvent(){
        GuavaEventBusContext.registerListener(new TestGuavaGuavaEventListener());
        GuavaEventBusContext.postEvent(new TestGuavaEvent());
    }


    @Test
    public void testEvent(){
        SpringContextHolder.publishEvent(new TestEvent("Test."));
    }
}
