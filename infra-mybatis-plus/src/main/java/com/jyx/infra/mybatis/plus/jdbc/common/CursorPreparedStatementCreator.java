package com.jyx.infra.mybatis.plus.jdbc.common;

import com.jyx.infra.asserts.Asserts;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Archforce
 * @since 2023/11/22 16:47
 */
public class CursorPreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

    private final String sql;

    private final int fetchSize;

    public CursorPreparedStatementCreator(String sql, int fetchSize) {
        Asserts.notNull(sql, () -> "SQL must not be null");
        Asserts.isTrue(fetchSize > 0, () -> "Create PreparedStatement by cursor mode,fetchSize must be greater 0.");

        this.sql = sql;
        this.fetchSize = fetchSize;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(this.sql);
        preparedStatement.setFetchSize(fetchSize);
        return preparedStatement;
    }

    @Override
    public String getSql() {
        return this.sql;
    }
}
