package com.jyx.infra.mybatis.plus.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Archforce
 * @since 2023/11/20 15:45
 */
@Getter
@AllArgsConstructor
public class JdbcProperties {

    /**
     * 任务规模：
     * 一次提交到线程池的任务包含多少条数据
     */
    private int taskSize;

    /**
     * 批次大小：
     * 插入时一次添加到Statement包含多少条数据
     * 查询时一次获取多少条数据
     */
    private int batchSize;

}
