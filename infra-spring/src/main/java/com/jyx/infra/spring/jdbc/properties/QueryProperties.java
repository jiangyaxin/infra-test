package com.jyx.infra.spring.jdbc.properties;

import lombok.Data;

/**
 * @author Archforce
 * @since 2023/11/28 16:30
 */
@Data
public class QueryProperties {

    interface Constants {

        int DEFAULT_QUERY_TASK_SIZE_OF_EACH_WORKER = 50_000;

        int DEFAULT_QUERY_ONCE_BATCH_SIZE_OF_EACH_WORKER = 5_000;
    }

    /**
     * 任务规模：
     * 一次提交到线程池的任务包含多少条数据
     */
    private int taskSizeOfEachWorker = Constants.DEFAULT_QUERY_TASK_SIZE_OF_EACH_WORKER;

    /**
     * 批次大小：
     * 查询时一次获取多少条数据
     */
    private int onceBatchSizeOfEachWorker = Constants.DEFAULT_QUERY_ONCE_BATCH_SIZE_OF_EACH_WORKER;
}
