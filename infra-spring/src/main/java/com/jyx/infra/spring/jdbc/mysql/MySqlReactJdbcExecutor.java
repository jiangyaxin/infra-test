package com.jyx.infra.spring.jdbc.mysql;

import com.jyx.infra.spring.jdbc.ReactJdbcExecutor;
import com.jyx.infra.spring.jdbc.mysql.reader.ReactMysqlJdbcReader;
import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiangyaxin
 * @since 2023/11/21 11:24
 */
public class MySqlReactJdbcExecutor extends ReactJdbcExecutor {

    @Override
    protected <OUT> JdbcReader<List<CompletableFuture<List<OUT>>>> buildJdbcReader(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor) {
        return new ReactMysqlJdbcReader<>(ioPool, taskPool, extractPostProcessor);
    }
}
