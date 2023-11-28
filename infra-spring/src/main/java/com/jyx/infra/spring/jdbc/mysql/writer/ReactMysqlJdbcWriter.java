package com.jyx.infra.spring.jdbc.mysql.writer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiangyaxin
 * @since 2023/11/27 16:02
 */
public class ReactMysqlJdbcWriter extends AbstractMysqlJdbcWriter {

    private final ThreadPoolExecutor taskPool;

    public ReactMysqlJdbcWriter(ThreadPoolExecutor ioPool, ThreadPoolExecutor taskPool) {
        super(ioPool);
        this.taskPool = taskPool;
    }

    @Override
    protected ExecutorService getDataExtractorPool() {
        return taskPool;
    }
}
