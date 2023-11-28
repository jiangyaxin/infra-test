package com.jyx.infra.spring.jdbc.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author jiangyaxin
 * @since 2023/11/25 11:52
 */
@Slf4j
public abstract class InterruptBatchInsertArgsSetter implements InterruptibleBatchPreparedStatementSetter {

    private final int batchSize;

    private boolean exhausted;

    private final String[] columns;

    private final int[] argTypes;

    private final boolean clearObjectArrayAfterSet;

    public InterruptBatchInsertArgsSetter(int batchSize, String[] columns, int[] argTypes,boolean clearObjectArrayAfterSet) {
        this.batchSize = batchSize;
        this.exhausted = false;
        this.columns = columns;
        this.argTypes = argTypes == null ? new int[0] : argTypes;
        this.clearObjectArrayAfterSet = clearObjectArrayAfterSet;
    }

    public void interrupt() {
        this.exhausted = true;
    }

    protected abstract boolean hasNextAvailableData(int i);

    protected abstract Object[] getCurrentData(int i);

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Object[] values = getCurrentData(i);
        if (values.length != columns.length) {
            throw new IncorrectInsertDataException(String.format("Batch insert set PreparedStatement args error,data length [%s] != columns length [%s]", i, values.length, columns.length));
        }

        int colIndex = 0;
        for (Object value : values) {
            colIndex++;
            if (value instanceof SqlParameterValue) {
                SqlParameterValue paramValue = (SqlParameterValue) value;
                StatementCreatorUtils.setParameterValue(ps, colIndex, paramValue, paramValue.getValue());
            } else {
                int colType;
                if (argTypes.length < colIndex) {
                    colType = SqlTypeValue.TYPE_UNKNOWN;
                } else {
                    colType = argTypes[colIndex - 1];
                }
                StatementCreatorUtils.setParameterValue(ps, colIndex, colType, value);
            }
        }
        if (clearObjectArrayAfterSet) {
            values = null;
        }
    }

    @Override
    public boolean isBatchExhausted(int i) {
        return exhausted || !hasNextAvailableData(i);
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }
}
