package com.jyx.infra.spring.jdbc.mysql;

import com.jyx.infra.spring.jdbc.MultiThreadJdbcExecutor;
import com.jyx.infra.spring.jdbc.mysql.reader.MultiThreadMysqlJdbcReader;
import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiangyaxin
 * @since 2023/11/21 11:24
 */
public class MySqlMultiThreadJdbcExecutor extends MultiThreadJdbcExecutor {

    @Override
    protected <OUT> JdbcReader<List<OUT>> buildJdbcReader(ThreadPoolExecutor ioPool, ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor) {
        return new MultiThreadMysqlJdbcReader<>(ioPool, extractPostProcessor);
    }
}
