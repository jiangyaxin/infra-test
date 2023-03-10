package com.jyx.infra.spring.config;

import com.jyx.infra.spring.context.AppConstant;
import com.jyx.infra.thread.ThreadPoolExecutors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Archforce
 * @since 2023/2/27 13:36
 */
@Configuration
public class PoolConfig {

    @PreDestroy
    public void destroy() {
        ThreadPoolExecutors.closeAll(20);
    }

    @Bean(AppConstant.IO_POOL_NAME)
    @ConditionalOnProperty(value = "app.pool.enabled-io-pool", havingValue = "true")
    ThreadPoolExecutor ioPool() {
        int processors = Runtime.getRuntime().availableProcessors();

        int factor = 10;
        int coreSize = processors * 2;
        int maxSize = processors * factor;
        int queueSize = 1024 * factor;
        ThreadPoolExecutor pool = ThreadPoolExecutors.newThreadPool(
                coreSize,
                maxSize,
                queueSize,
                AppConstant.IO_POOL_NAME
        );
        return pool;
    }

    @Bean(AppConstant.CALCULATE_POOL_NAME)
    @ConditionalOnProperty(value = "app.pool.enabled-calculate-pool", havingValue = "true")
    ThreadPoolExecutor calculatePool() {
        int processors = Runtime.getRuntime().availableProcessors();

        int factor = 2;
        int coreSize = processors + 1;
        int maxSize = processors * factor;
        int queueSize = 256 * factor;
        ThreadPoolExecutor pool = ThreadPoolExecutors.newThreadPool(
                coreSize,
                maxSize,
                queueSize,
                AppConstant.CALCULATE_POOL_NAME
        );
        return pool;
    }
}
