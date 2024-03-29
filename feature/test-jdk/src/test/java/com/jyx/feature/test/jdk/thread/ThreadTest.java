package com.jyx.feature.test.jdk.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jyx.infra.thread.PriorityThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author jiangyaxin
 * @since 2023/4/7 15:55
 */
@Slf4j
public class ThreadTest {

    @Test
    public void completableFutureTest() throws InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3_000);
                log.info("我是Future1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        future1.thenRun(() -> log.info("我是Future1的大儿子"));
        future1.thenRun(() -> log.info("我是Future1的二儿子"));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(5_000);
                log.info("我是Future2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10_000);
                log.info("我是Future3");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(2_000);
        future1.cancel(true);
        future2.cancel(true);
        future3.cancel(true);

        Thread.sleep(20_000);
    }

    public static void main(String[] args) {
        Executors.newFixedThreadPool(1).submit(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("我还在运行");
            }
        });

        log.info("我马上结束运行");
    }

    @Test
    public void priorityPoolTest() throws Exception {
        int coreSize = Math.max(Runtime.getRuntime().availableProcessors(), 4);
        int maxSize = coreSize * 2;


        PriorityThreadPoolExecutor threadPoolExecutor = new PriorityThreadPoolExecutor(
                1,
                maxSize,
                2,
                TimeUnit.MINUTES,
                5,
                new ThreadFactoryBuilder().setNameFormat("TEST").build(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        List<Future<Integer>> futureList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            if (i % 3 == 0) {
                Future<Integer> submit = threadPoolExecutor.submit(() -> {
                    try {
                        Thread.sleep(finalI * 1000);
                        log.info("我是{}", finalI);
                        return finalI;
                    } catch (Exception e) {
                        log.error("", e);
                        return -1;
                    }
                },finalI);
                futureList.add(submit);
            } else if (i % 3 == 1)  {
                threadPoolExecutor.submit(() -> {
                    try {
                        Thread.sleep(finalI * 1000);
                        log.info("我是{}", finalI);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                },finalI);
            } else {
                threadPoolExecutor.execute(() -> {
                    try {
                        Thread.sleep(finalI * 1000);
                        log.info("我是{}", finalI);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                },finalI);
            }
        }

        for (Future<Integer> future : futureList) {
            log.info("返回结果 {}", future.get());
        }

        CompletableFuture.runAsync(() -> log.info("---------"), threadPoolExecutor);

        Thread.currentThread().join(30 * 1000);
    }


    @Test
    public void treadTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("我还在运行");
                // interrupt 打断sleep、join等抛出InterruptedException异常后，interrupt标志会被清除
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.warn("线程中断", e);
                    Thread.currentThread().interrupt();
                }
            }
            log.info("我已经停止运行");
        });
        thread.start();

        Thread.sleep(10 * 1000);
        thread.interrupt();
        Thread.sleep(10 * 1000);
    }

    @Test
    public void stackTest() {
        System.out.println(getCallLocationName(4));
    }

    public static String getCallLocationName(int depth) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length <= depth) {
            return "<unknown>";
        }

        StackTraceElement elem = stackTrace[depth];

        return String.format(
                "%s(%s:%d)", elem.getMethodName(), elem.getFileName(), elem.getLineNumber());
    }
}
