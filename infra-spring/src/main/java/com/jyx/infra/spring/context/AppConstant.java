package com.jyx.infra.spring.context;


import com.jyx.infra.id.SnowflakeIdAllocator;

/**
 * @author JYX
 * @since 2021/11/5 15:25
 */
public interface AppConstant {

    char SEPARATOR = '#';

    String IO_POOL_NAME = "IO-Pool";
    String CALCULATE_POOL_NAME = "Calculate-Pool";

    AppContext APP_CONTEXT = SpringContextHolder.getBean(AppContext.class);

    ClusterContext CLUSTER_CONTEXT = APP_CONTEXT.getCluster();

    String MODULE = String.format("%s-%s-%s",
            SpringContextHolder.getApplicationContext().getEnvironment().getProperty("spring.application.name"),
            CLUSTER_CONTEXT.getDataCenterId(),
            CLUSTER_CONTEXT.getWorkerId()
    );

    SnowflakeIdAllocator<Long> ID_ALLOCATOR = SpringContextHolder.getBean(SnowflakeIdAllocator.class);
}
