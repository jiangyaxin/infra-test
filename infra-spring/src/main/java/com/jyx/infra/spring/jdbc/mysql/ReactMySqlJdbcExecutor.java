package com.jyx.infra.spring.jdbc.mysql;

import com.jyx.infra.spring.jdbc.ReactJdbcExecutor;
import com.jyx.infra.spring.jdbc.mysql.reader.ReactMysqlJdbcReader;
import com.jyx.infra.spring.jdbc.mysql.writer.MultiThreadMysqlJdbcWriter;
import com.jyx.infra.spring.jdbc.mysql.writer.ReactMysqlJdbcWriter;
import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.spring.jdbc.writer.JdbcWriter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author jiangyaxin
 * @since 2023/11/21 11:24
 */
public class ReactMySqlJdbcExecutor extends ReactJdbcExecutor {

    @Override
    protected <OUT> JdbcReader<List<CompletableFuture<List<OUT>>>> buildJdbcReader(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor) {
        return new ReactMysqlJdbcReader<>(ioPool, taskPool, extractPostProcessor);
    }

    @Override
    protected JdbcWriter buildJdbcWriter() {
        return new ReactMysqlJdbcWriter(ioPool, taskPool);
    }
}
