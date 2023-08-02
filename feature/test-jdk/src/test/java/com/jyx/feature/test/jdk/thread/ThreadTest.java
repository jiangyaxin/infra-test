package com.jyx.feature.test.jdk.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jyx.feature.test.jdk.concurrent.pool.PriorityThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Archforce
 * @since 2023/4/7 15:55
 */
@Slf4j
public class ThreadTest {

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
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            Future<Integer> submit = threadPoolExecutor.submit(() -> {
                try {
                    Thread.sleep(finalI * 1000);
                    log.info("我是{}", finalI);
                    return finalI;
                } catch (Exception e) {
                    log.error("", e);
                    return -1;
                }
            });
            futureList.add(submit);
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
