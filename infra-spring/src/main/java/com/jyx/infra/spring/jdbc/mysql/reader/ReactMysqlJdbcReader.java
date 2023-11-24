package com.jyx.infra.spring.jdbc.mysql.reader;

import com.jyx.infra.collection.Tuple2;
import com.jyx.infra.spring.jdbc.reader.AsyncResultSetExtractor;
import com.jyx.infra.spring.jdbc.reader.CursorPreparedStatementCreator;
import com.jyx.infra.spring.jdbc.reader.ResultSetExtractPostProcessor;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiangyaxin
 * @since 2023/11/24 14:14
 */
public class ReactMysqlJdbcReader<T> extends AbstractMysqlJdbcReader<List<CompletableFuture<List<T>>>> {

    private final ThreadPoolExecutor taskPool;

    private final ResultSetExtractPostProcessor<Object[], T> extractPostProcessor;

    public ReactMysqlJdbcReader(ThreadPoolExecutor ioPool,
                                ThreadPoolExecutor taskPool,
                                ResultSetExtractPostProcessor<Object[], T> extractPostProcessor) {
        super(ioPool);
        this.taskPool = taskPool;
        this.extractPostProcessor = extractPostProcessor;
    }

    @Override
    protected List<CompletableFuture<List<T>>> queryByIdRange(JdbcTemplate jdbcTemplate, Tuple2<String, Object[]> sqlPair, int onceBatchSizeOfEachWorker) {
        PreparedStatementCreator psc = new CursorPreparedStatementCreator(sqlPair.getKey(), onceBatchSizeOfEachWorker);
        ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(sqlPair.getValue());
        AsyncResultSetExtractor<T> resultSetExtractor = new AsyncResultSetExtractor<>(extractPostProcessor, onceBatchSizeOfEachWorker, taskPool);
        List<CompletableFuture<List<T>>> futureList = jdbcTemplate.query(psc, pss, resultSetExtractor);
        return futureList;
    }
}
