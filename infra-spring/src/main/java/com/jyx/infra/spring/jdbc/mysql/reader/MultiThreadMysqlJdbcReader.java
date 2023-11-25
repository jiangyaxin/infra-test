package com.jyx.infra.spring.jdbc.mysql.reader;

import com.jyx.infra.collection.Tuple2;
import com.jyx.infra.spring.jdbc.reader.CursorPreparedStatementCreator;
import com.jyx.infra.spring.jdbc.reader.IncorrectInstanceDataAccessException;
import com.jyx.infra.spring.jdbc.reader.ObjectArrayRowMapper;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import com.jyx.infra.util.CheckResult;
import org.springframework.jdbc.core.*;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiangyaxin
 * @since 2023/11/24 14:14
 */
public class MultiThreadMysqlJdbcReader<T> extends AbstractMysqlJdbcReader<List<T>> {

    private final ResultSetExtractPostProcessor<Object[], T> extractPostProcessor;

    private final ObjectArrayRowMapper objectArrayRowMapper;

    public MultiThreadMysqlJdbcReader(ThreadPoolExecutor ioPool,
                                      ResultSetExtractPostProcessor<Object[], T> extractPostProcessor) {
        super(ioPool);
        this.extractPostProcessor = extractPostProcessor;
        this.objectArrayRowMapper = new ObjectArrayRowMapper();
    }

    @Override
    protected List<T> queryByIdRange(JdbcTemplate jdbcTemplate, Tuple2<String, Object[]> sqlPair, int onceBatchSizeOfEachWorker) {
        PreparedStatementCreator psc = new CursorPreparedStatementCreator(sqlPair.getKey(), onceBatchSizeOfEachWorker);
        ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(sqlPair.getValue());

        RowMapper<T> objectRowMapper = (rs, rowNum) -> {
            Object[] objects = objectArrayRowMapper.mapRow(rs, rowNum);
            if (rowNum == 0) {
                CheckResult checkResult = extractPostProcessor.canProcess(objects);
                if (!checkResult.isSuccess()) {
                    throw new IncorrectInstanceDataAccessException(checkResult.getMessage());
                }
            }

            T instance = extractPostProcessor.process(objects);
            objects = null;
            return instance;
        };
        RowMapperResultSetExtractor<T> resultSetExtractor = new RowMapperResultSetExtractor<>(objectRowMapper);

        List<T> result = jdbcTemplate.query(psc, pss, resultSetExtractor);
        return result;
    }
}
