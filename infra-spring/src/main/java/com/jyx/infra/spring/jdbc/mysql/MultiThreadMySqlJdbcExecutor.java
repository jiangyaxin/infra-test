package com.jyx.infra.spring.jdbc.mysql;

import com.jyx.infra.spring.jdbc.MultiThreadJdbcExecutor;
import com.jyx.infra.spring.jdbc.mysql.reader.MultiThreadMysqlJdbcReader;
import com.jyx.infra.spring.jdbc.reader.JdbcReader;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/11/21 11:24
 */
public class MultiThreadMySqlJdbcExecutor extends MultiThreadJdbcExecutor {

    @Override
    protected <OUT> JdbcReader<List<OUT>> buildJdbcReader(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor) {
        return new MultiThreadMysqlJdbcReader<>(ioPool, extractPostProcessor);
    }
}
