package com.jyx.infra.serializable;

import com.jyx.infra.datetime.StopWatch;
import com.jyx.infra.log.Logs;
import com.jyx.infra.thread.FutureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * @author Archforce
 * @since 2023/9/28 15:02
 */
@Slf4j
public abstract class AbstractSerializer implements Serializer {

    protected final SerializeContext serializeContext;

    public AbstractSerializer(SerializeContext serializeContext) {
        serializeContext.validateParameter();

        this.serializeContext = serializeContext;
    }


    @Override
    public <T> List<T> deserialize(Class<T> clazz, Collection<String> filePathCollection) {
        List<CompletableFuture<FutureResult<List<T>>>> futureList = deserializeSync(ForkJoinPool.commonPool(), clazz, filePathCollection);

        int resultSize = 0;
        List<FutureResult<List<T>>> futureResultList = new ArrayList<>(futureList.size());
        for (CompletableFuture<FutureResult<List<T>>> future : futureList) {
            try {
                FutureResult<List<T>> futureResult = future.get();
                if (futureResult.exceptionally()) {
                    throw futureResult.getThrowable();
                }
                resultSize += futureResult.getData().size();
                futureResultList.add(futureResult);
            } catch (Throwable e) {
                throw new SerializableException(e);
            }
        }

        List<T> result = new ArrayList<>(resultSize);
        futureResultList.stream()
                .map(FutureResult::getData)
                .forEach(result::addAll);
        return result;
    }

    @Override
    public <T> List<T> deserialize(Class<T> clazz, String... filePaths) {
        return deserialize(clazz, List.of(filePaths));
    }

    @Override
    public <T> CompletableFuture<FutureResult<List<T>>> deserializeSync(ExecutorService executorService, Class<T> clazz, String filePath) {
        return CompletableFuture.supplyAsync(() -> deserialize(clazz, filePath), executorService)
                .handleAsync((result, ex) -> {
                    if (ex != null) {
                        Logs.error(log, "Deserialize failed.", ex);
                    }
                    return FutureResult.of(result, ex);
                }, executorService);
    }

    @Override
    public <T> List<CompletableFuture<FutureResult<List<T>>>> deserializeSync(ExecutorService executorService, Class<T> clazz, Collection<String> filePathCollection) {
        return filePathCollection.stream()
                .map(filePath -> deserializeSync(executorService, clazz, filePath))
                .collect(Collectors.toList());
    }

    @Override
    public <T> List<CompletableFuture<FutureResult<List<T>>>> deserializeSync(ExecutorService executorService, Class<T> clazz, String... filePaths) {
        return deserializeSync(executorService, clazz, List.of(filePaths));
    }

    protected void deleteOldFile(String filePath) {
        File file = new File(filePath);
        File parentFile = file.getParentFile();

        if (file.exists()) {
            if (!file.delete()) {
                throw new SerializableException(String.format("Delete file %s failed", file.getAbsoluteFile()));
            }
        } else {
            if (!parentFile.exists()) {
                if (!parentFile.mkdir()) {
                    throw new SerializableException(String.format("Delete directory %s failed", parentFile.getAbsoluteFile()));
                }
            }
        }
    }

    protected <T> void printSerializeSuccessLog(String filePath, ArrayList<T> dataList, StopWatch stopWatch, long fileSize) {
        String tps = calculateSerializeTps(stopWatch, fileSize);

        if (dataList == null || dataList.size() == 0) {
            Logs.info(log, "Serialize successes,file {},spend {}ms,bytes {}B,tps= {}MB/s",
                    filePath, stopWatch.getTotalTimeMillis(), fileSize, tps);
        } else {
            Class<?> clazz = dataList.get(0).getClass();
            Logs.info(log, "Serialize successes,instance {},file {},spend {}ms,bytes {}B,tps= {}MB/s",
                    clazz.getName(), filePath, stopWatch.getTotalTimeMillis(), fileSize, tps);
        }
    }

    protected <T> void printDeserializeSuccessLog(String filePath, Class<T> clazz, StopWatch stopWatch, long fileSize) {
        String tps = calculateSerializeTps(stopWatch, fileSize);

        Logs.info(log, "Deserialize successes,instance {},file {},spend {}ms,bytes {}B,tps= {}MB/s",
                clazz.getName(), filePath, stopWatch.getTotalTimeMillis(), fileSize, tps);
    }

    protected void closeMappedByteBuffer(MappedByteBuffer mappedByteBuffer) {
        if (mappedByteBuffer == null) {
            return;
        }

        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                Class<? extends MappedByteBuffer> clazz = mappedByteBuffer.getClass();
                Method cleanerMethod = ReflectionUtils.findMethod(clazz, "cleaner");
                if (cleanerMethod == null) {
                    throw new SerializableException(String.format("Get method %s#cleaner() failed.", clazz.getName()));
                }

                cleanerMethod.setAccessible(true);
                Object cleaner = cleanerMethod.invoke(mappedByteBuffer);
                if (cleaner == null) {
                    throw new SerializableException(String.format("%s get Cleaner failed.", clazz.getName()));
                }

                Class<?> cleanerClazz = cleaner.getClass();
                Method cleanMethod = ReflectionUtils.findMethod(cleanerClazz, "clean");
                if (cleanerMethod == null) {
                    throw new SerializableException(String.format("Get method %s#clean() failed.", cleanerClazz.getName()));
                }
                cleanerMethod.setAccessible(true);

                cleanMethod.invoke(cleaner);
            } catch (Exception e) {
                throw new SerializableException("Closed MappedByteBuffer failed.", e);
            }

            return null;
        });
    }

    private String calculateSerializeTps(StopWatch stopWatch, long fileSize) {
        DecimalFormat numberFormat = new DecimalFormat("#.##");
        return numberFormat.format(fileSize / (double) stopWatch.getTotalTimeNanos() * 1000_000_000 / 1024 / 1024);
    }
}
