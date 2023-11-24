package com.jyx.infra.spring.jdbc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2023/11/20 15:45
 */
@Data
@Component
@ConfigurationProperties(prefix = JdbcProperties.Constants.PROPERTIES_PREFIX)
public class JdbcProperties {

    interface Constants {

        String PROPERTIES_PREFIX = "jdbc";

        int DEFAULT_TASK_SIZE = 50000;

        int DEFAULT_BATCH_SIZE = 5000;
    }

    /**
     * 任务规模：
     * 一次提交到线程池的任务包含多少条数据
     */
    private int taskSize = Constants.DEFAULT_TASK_SIZE;

    /**
     * 批次大小：
     * 插入时一次添加到Statement包含多少条数据
     * 查询时一次获取多少条数据
     */
    private int batchSize = Constants.DEFAULT_BATCH_SIZE;

}
