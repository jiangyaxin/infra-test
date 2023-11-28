package com.jyx.infra.spring.jdbc.writer;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author jiangyaxin
 * @since 2023/11/25 15:32
 */
public class InterruptBatchInsertPreparedStatementCallBack implements PreparedStatementCallback<Integer> {

    private final InterruptibleBatchPreparedStatementSetter pss;

    public InterruptBatchInsertPreparedStatementCallBack(InterruptibleBatchPreparedStatementSetter pss) {
        this.pss = pss;
    }

    @Override
    public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        try {
            int batchSize = pss.getBatchSize();
            if (JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
                return batchExecute(ps, batchSize);
            } else {
                return oneByOneExecute(ps);
            }
        } finally {
            if (pss instanceof ParameterDisposer) {
                ((ParameterDisposer) pss).cleanupParameters();
            }
        }
    }

    private int batchExecute(PreparedStatement ps, int batchSize) throws SQLException {
        int count = 0;
        int i = 0;
        while (!pss.isBatchExhausted(i)) {
            for (int indexOfBatch = 0; indexOfBatch < batchSize; indexOfBatch++) {
                if (pss.isBatchExhausted(i)) {
                    break;
                }
                pss.setValues(ps, i);
                ps.addBatch();
                i++;
            }
            int[] rowsAffected = ps.executeBatch();
            count += Arrays
                    .stream(rowsAffected)
                    .map(affected -> affected == PreparedStatement.SUCCESS_NO_INFO ? 1 : affected)
                    .sum();
        }
        return count;
    }

    private int oneByOneExecute(PreparedStatement ps) throws SQLException {
        int count = 0;
        int i = 0;
        while (!pss.isBatchExhausted(i)) {
            pss.setValues(ps, i);
            count += ps.executeUpdate();
            i++;
        }
        return count;
    }
}
