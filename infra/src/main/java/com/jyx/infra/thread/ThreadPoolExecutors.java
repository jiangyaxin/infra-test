package com.jyx.infra.thread;

import com.jyx.infra.exception.AppException;
import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Archforce
 * @since 2023/2/27 11:23
 */
@Slf4j
public class ThreadPoolExecutors {

    private static final Map<String, ThreadPoolExecutor> CURRENT_POOL_MAP = new ConcurrentHashMap<>();

    public static ThreadPoolExecutor getThreadPool(String poolName) {
        ThreadPoolExecutor pool = CURRENT_POOL_MAP.get(poolName);
        if (pool == null) {
            throw new AppException(String.format("ThreadPool(%s) not created", poolName));
        }
        return pool;
    }

    public static synchronized ThreadPoolExecutor newThreadPool(int corePoolSize, int maxPoolSize, int queueCapacity, String poolName) {
        if (CURRENT_POOL_MAP.containsKey(poolName)) {
            throw new AppException(String.format("ThreadPool(%s) already existed", poolName));
        }
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                5,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(queueCapacity),
                new NamingThreadFactory(poolName),
                new ThreadPoolExecutor.AbortPolicy()
        );
        CURRENT_POOL_MAP.put(poolName, pool);
        Logs.info(log, "ThreadPool(name={},core={},max={},queueCapacity={}) created", poolName, corePoolSize, maxPoolSize, queueCapacity);
        return pool;
    }

    public static synchronized void closeAll(int shutdownTimeout) {
        if (CURRENT_POOL_MAP.isEmpty()) {
            return;
        }
        CompletableFuture[] closeFutureArray = CURRENT_POOL_MAP.entrySet()
                .stream()
                .map(poolEntry -> CompletableFuture.runAsync(() -> {
                            try {
                                String name = poolEntry.getKey();
                                ThreadPoolExecutor pool = poolEntry.getValue();

                                pool.shutdown();
                                if (!pool.awaitTermination(shutdownTimeout, TimeUnit.MILLISECONDS)) {
                                    List<Runnable> remainRunableList = pool.shutdownNow();

                                    Logs.warn(log, "ThreadPool({}) has {} tasks not completed", name, remainRunableList.size());
                                }

                                Logs.info(log, "ThreadPool({}) stopped", name);
                            } catch (InterruptedException e) {
                                throw new AppException(e);
                            }
                        }

                ))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(closeFutureArray).join();
    }
}
