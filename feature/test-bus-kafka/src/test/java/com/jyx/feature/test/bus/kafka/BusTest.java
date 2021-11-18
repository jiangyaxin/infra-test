package com.jyx.feature.test.bus.kafka;

import com.jyx.infra.bus.context.BusContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JYX
 * @since 2021/11/18 10:55
 */
@Slf4j
@SpringBootTest
public class BusTest {

    @Test
    public void testBusContext(){
        BusContextHolder.publishGlobalEvent(new TestBusEvent());
    }
}
