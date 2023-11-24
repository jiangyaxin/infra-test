package com.jyx.infra.spring.jdbc.reader;

import com.jyx.infra.asserts.Asserts;
import com.jyx.infra.util.CheckResult;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * @author jiangyaxin
 * @since 2023/11/22 16:55
 */
public class AsyncResultSetExtractor<OUT> implements ResultSetExtractor<List<CompletableFuture<List<OUT>>>> {

    private final ExecutorService executorService;

    private final ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor;

    private final ObjectArrayRowMapper rowMapper;

    private final int batchSize;

    private final int taskExpected;

    public AsyncResultSetExtractor(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor, int batchSize) {
        this(extractPostProcessor, batchSize, ForkJoinPool.commonPool());
    }

    public AsyncResultSetExtractor(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor, int batchSize, ExecutorService executorService) {
        this(extractPostProcessor, 0, batchSize, executorService);
    }

    public AsyncResultSetExtractor(ResultSetExtractPostProcessor<Object[], OUT> extractPostProcessor, int taskExpected, int batchSize, ExecutorService executorService) {
        Asserts.notNull(extractPostProcessor, () -> "resultSetExtractPostProcessor is required");
        Asserts.notNull(executorService, () -> "executorService is required");
        Asserts.isTrue(batchSize > 0, () -> "In CompletableFutureResultSetExtractor,batchSize must be greater 0.");
        this.rowMapper = new ObjectArrayRowMapper();
        this.batchSize = batchSize;
        this.taskExpected = taskExpected;
        this.extractPostProcessor = extractPostProcessor;
        this.executorService = executorService;
    }


    @Override
    public List<CompletableFuture<List<OUT>>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<CompletableFuture<List<OUT>>> resultList = (taskExpected > 0 ? new ArrayList<>(taskExpected) : new ArrayList<>(128));

        int batchIndex = 0;
        int rowNum = 0;

        List<Object[]> rowBatchList = new ArrayList<>(batchSize);
        while (rs.next()) {
            Object[] objects = rowMapper.mapRow(rs, rowNum);
            if (rowNum == 0) {
                CheckResult checkResult = extractPostProcessor.canProcess(objects);
                if (!checkResult.isSuccess()) {
                    throw new IncorrectInstanceDataAccessException(checkResult.getMessage());
                }
            }
            rowBatchList.add(objects);
            rowNum++;
            batchIndex++;

            if (batchIndex == batchSize) {
                CompletableFuture<List<OUT>> future = submit(rowBatchList);
                resultList.add(future);

                batchIndex = 0;
                rowBatchList = new ArrayList<>(batchSize);
            }
        }

        if (!CollectionUtils.isEmpty(rowBatchList)) {
            CompletableFuture<List<OUT>> future = submit(rowBatchList);
            resultList.add(future);
        }

        return resultList;
    }

    private CompletableFuture<List<OUT>> submit(List<Object[]> rowBatchList) {
        List<Object[]> localRowBatchList = rowBatchList;
        CompletableFuture<List<OUT>> future = CompletableFuture.supplyAsync(() -> {
            List<OUT> outBatchList = new ArrayList<>(batchSize);

            Iterator<Object[]> it = localRowBatchList.iterator();
            while (it.hasNext()) {
                OUT out = extractPostProcessor.process(it.next());
                outBatchList.add(out);
                it.remove();
            }
            return outBatchList;
        }, executorService);
        return future;
    }
}
