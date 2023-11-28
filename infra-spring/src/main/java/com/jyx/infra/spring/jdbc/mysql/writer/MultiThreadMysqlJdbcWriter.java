package com.jyx.infra.spring.jdbc.mysql.writer;

import com.jyx.infra.spring.jdbc.writer.AbstractJdbcWriter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Archforce
 * @since 2023/11/27 16:02
 */
public class MultiThreadMysqlJdbcWriter extends AbstractMysqlJdbcWriter {

    public MultiThreadMysqlJdbcWriter(ThreadPoolExecutor ioPool) {
        super(ioPool);
    }

    @Override
    protected ExecutorService getDataExtractorPool() {
        return ioPool;
    }
}
