package com.jyx.infra.context;

import com.jyx.infra.id.SnowflakeIdAllocator;

/**
 * @author JYX
 * @since 2021/11/5 15:25
 */
public interface AppConstant {

    AppContext APP_CONTEXT = SpringContextHolder.getBean(AppContext.class);

    ClusterContext CLUSTER_CONTEXT = APP_CONTEXT.getCluster();

    String MODULE = String.format("%s-%s-%s",
            SpringContextHolder.getApplicationContext().getEnvironment().getProperty("spring.application.name"),
            CLUSTER_CONTEXT.getDataCenterId(),
            CLUSTER_CONTEXT.getWorkerId()
    );

    SnowflakeIdAllocator<Long> ID_ALLOCATOR = SpringContextHolder.getBean(SnowflakeIdAllocator.class);
}