package com.jyx.infra.spring.jdbc.writer;

import com.google.common.collect.Lists;
import com.jyx.infra.log.Logs;
import com.jyx.infra.thread.FutureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author Archforce
 * @since 2023/11/27 15:03
 */
@Slf4j
public class AsyncReflectInsertDataExtractor implements InsertDataExtractor<List<CompletableFuture<List<Object[]>>>> {

    private final Field[] fields;

    private final ExecutorService executorService;

    private final int taskSizeOfEachWorker;


    public AsyncReflectInsertDataExtractor(Field[] fields,
                                           ExecutorService executorService,
                                           int taskSizeOfEachWorker) {
        this.fields = fields;
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
        }
        this.executorService = executorService;
        this.taskSizeOfEachWorker = taskSizeOfEachWorker;
    }

    @Override
    public <T> List<CompletableFuture<List<Object[]>>> extract(List<T> dataList) {
        List<List<T>> partitionList = Lists.partition(dataList, taskSizeOfEachWorker);
        List<CompletableFuture<List<Object[]>>> futureList = new ArrayList<>(partitionList.size());

        for (List<T> partition : partitionList) {
            CompletableFuture<List<Object[]>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    List<Object[]> objectsList = new ArrayList<>();
                    for (T data : partition) {
                        Object[] objects = new Object[fields.length];
                        for (int i = 0; i < fields.length; i++) {
                            objects[i] = fields[i].get(data);
                        }
                        objectsList.add(objects);
                    }
                    return objectsList;
                } catch (Exception e) {
                    Logs.error(log, "AsyncReflectInsertData error.", e);
                    throw FutureException.of("AsyncReflectInsertData error", e);
                }
            }, executorService);
            futureList.add(future);
        }
        return futureList;
    }
}
