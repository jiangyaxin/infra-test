package com.jyx.infra.spring.config;

import com.jyx.infra.id.IdAllocators;
import com.jyx.infra.id.SnowflakeIdAllocator;
import com.jyx.infra.spring.context.AppContext;
import com.jyx.infra.spring.context.ClusterContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author jiangyaxin
 * @since 2021/10/20 21:49
 */
@Configuration
public class IdConfig {

    @Bean
    SnowflakeIdAllocator<Long> idAllocator(AppContext appContext) {
        ClusterContext clusterContext = appContext.getCluster();
        Integer dataCenterId = clusterContext.getDataCenterId();
        Integer workerId = clusterContext.getWorkerId();

        Assert.notNull(dataCenterId, "app.cluster.data-center-id properties is null");
        Assert.notNull(workerId, "app.cluster.worker-id properties is null");

        boolean enabledCachedIdGenerator = appContext.getCluster().isEnabledCachedIdGenerator();
        if (enabledCachedIdGenerator) {
            return IdAllocators.ofCachedSnowflake(dataCenterId, workerId);
        } else {
            return IdAllocators.ofDefaultSnowflake(dataCenterId, workerId);
        }
    }
}
