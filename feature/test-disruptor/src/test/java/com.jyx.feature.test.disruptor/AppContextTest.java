package com.jyx.feature.test.disruptor;

import com.jyx.infra.spring.context.AppContext;
import com.jyx.infra.spring.context.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadPoolExecutor;

import static com.jyx.infra.spring.context.AppConstant.*;

/**
 * @author jiangyaxin
 * @since 2021/10/20 22:42
 */
@Slf4j
@SpringBootTest
public class AppContextTest {

    @Autowired
    private AppContext appContext;

    @Autowired
    @Qualifier(IO_POOL_NAME)
    private ThreadPoolExecutor ioPoolAutowired;

    @Autowired
    @Qualifier(CALCULATE_POOL_NAME)
    private ThreadPoolExecutor calculatePoolAutowired;

    @Test
    public void testAppContext() {
        Integer workerId = appContext.getCluster().getWorkerId();
        Integer dataCenterId = appContext.getCluster().getDataCenterId();

        Long id = ID_ALLOCATOR.getId();

        Assertions.assertNotNull(workerId);
        Assertions.assertNotNull(dataCenterId);
        Assertions.assertNotNull(id);

    }

    @Test
    public void ioPoolTest() {
        ThreadPoolExecutor ioPool = SpringContextHolder.getBean(IO_POOL_NAME);
        ThreadPoolExecutor calculatePool = SpringContextHolder.getBean(CALCULATE_POOL_NAME);

        Assertions.assertNotNull(ioPool);
        Assertions.assertNotNull(calculatePool);
    }

}
