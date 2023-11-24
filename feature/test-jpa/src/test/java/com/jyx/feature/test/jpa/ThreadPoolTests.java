package com.jyx.feature.test.jpa;

import com.jyx.infra.thread.NamingThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangyaxin
 * @since 2022/12/13 13:27
 */
@Slf4j
public class ThreadPoolTests {

    @Test
    public void cancelTest() throws Exception {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamingThreadFactory("cancel-test"));

        Map<Integer, Future<?>> futureMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Future<?> future = threadPoolExecutor.submit(() -> {
                log.info("task{}", finalI);
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            futureMap.put(i, future);
        }

        Thread.currentThread().join(10 * 1000);

        futureMap.get(5).cancel(true);
        futureMap.get(1).cancel(true);
    }
}
