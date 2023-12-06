package com.jyx.infra.spring.jdbc.properties;

import lombok.Data;

/**
 * @author jiangyaxin
 * @since 2023/11/28 16:30
 */
@Data
public class InsertProperties {

    interface Constants {

        int DEFAULT_INSERT_TASK_SIZE_OF_EACH_WORKER = 5_000;

        int DEFAULT_INSERT_ONCE_BATCH_SIZE_OF_EACH_WORKER = 500;
    }

    /**
     * 任务规模：
     * 一次提交到线程池的任务包含多少条数据
     */
    private int taskSizeOfEachWorker = Constants.DEFAULT_INSERT_TASK_SIZE_OF_EACH_WORKER;

    /**
     * 批次大小：
     * 插入时一次添加到Statement包含多少条数据
     */
    private int onceBatchSizeOfEachWorker = Constants.DEFAULT_INSERT_ONCE_BATCH_SIZE_OF_EACH_WORKER;
}
