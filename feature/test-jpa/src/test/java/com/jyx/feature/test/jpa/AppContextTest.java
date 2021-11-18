package com.jyx.feature.test.jpa;

import com.jyx.infra.context.AppContext;
import com.jyx.infra.context.SpringContextHolder;
import com.jyx.infra.event.GuavaEventBusContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jyx.infra.context.AppConstant.ID_ALLOCATOR;

/**
 * @author JYX
 * @since 2021/10/20 22:42
 */
@Slf4j
@SpringBootTest
public class AppContextTest {

    @Autowired
    AppContext appContext;

    @Test
    public void testAppContext(){
        Integer workerId = appContext.getCluster().getWorkerId();
        Integer dataCenterId = appContext.getCluster().getDataCenterId();

        Long id = ID_ALLOCATOR.getId();

        Assertions.assertNotNull(workerId);
        Assertions.assertNotNull(dataCenterId);
        Assertions.assertNotNull(id);

    }

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
