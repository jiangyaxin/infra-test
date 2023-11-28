package com.jyx.infra.spring.jdbc.mysql.writer;

import com.jyx.infra.constant.StringConstant;
import com.jyx.infra.spring.jdbc.mysql.SqlHelper;
import com.jyx.infra.spring.jdbc.writer.AbstractJdbcWriter;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.jyx.infra.spring.jdbc.KeyWorkConstant.PLACEHOLDER;
import static com.jyx.infra.spring.jdbc.mysql.writer.AbstractMysqlJdbcWriter.Constants.INSERT_SQL;

/**
 * @author jiangyaxin
 * @since 2023/11/27 17:28
 */
public abstract class AbstractMysqlJdbcWriter extends AbstractJdbcWriter {

    interface Constants {
        String INSERT_SQL = "INSERT %s (%s) VALUES (%s)";
    }

    public AbstractMysqlJdbcWriter(ThreadPoolExecutor ioPool) {
        super(ioPool);
    }

    @Override
    protected String buildInsertSql(String tableName, String[] columns) {
        String tmpTableName = SqlHelper.formatKeyWork(tableName);
        String tmpColumns = Arrays.stream(columns)
                .map(SqlHelper::formatKeyWork)
                .collect(Collectors.joining(StringConstant.COMMA));
        String args = Arrays.stream(columns)
                .map(column -> PLACEHOLDER)
                .collect(Collectors.joining(StringConstant.COMMA));

        String insertSql = String.format(INSERT_SQL, tmpTableName, tmpColumns, args);
        return insertSql;
    }
}
