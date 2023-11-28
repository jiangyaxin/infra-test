package com.jyx.infra.spring.jdbc.writer;

import com.jyx.infra.spring.jdbc.Insert;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Archforce
 * @since 2023/11/25 11:03
 */
public interface JdbcWriter {

    <T> int batchInsert(JdbcTemplate jdbcTemplate,
                        List<T> dataList, Field[] fields,
                        Insert insert,
                        int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    <T> BatchInsertResult batchInsertAsync(JdbcTemplate jdbcTemplate,
                                           List<T> dataList, Field[] fields,
                                           Insert insert,
                                           int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    int batchInsert(JdbcTemplate jdbcTemplate,
                    List<Object[]> dataList,
                    Insert insert,
                    int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);

    BatchInsertResult batchInsertAsync(JdbcTemplate jdbcTemplate,
                                       List<Object[]> dataList,
                                       Insert insert,
                                       int taskSizeOfEachWorker, int onceBatchSizeOfEachWorker);
}
