package com.jyx.feature.test.jpa;

import com.jyx.infra.context.AppContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JYX
 * @since 2021/10/20 22:42
 */
@SpringBootTest
public class AppContextTest {

    @Autowired
    AppContext appContext;

    @Test
    public void testAppContext(){
        Integer workerId = appContext.getCluster().getWorkerId();
        Integer dataCenterId = appContext.getCluster().getDataCenterId();

        Assertions.assertNotNull(workerId);
        Assertions.assertNotNull(dataCenterId);
    }
}
