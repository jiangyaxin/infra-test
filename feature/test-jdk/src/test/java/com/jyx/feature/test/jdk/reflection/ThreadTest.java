package com.jyx.feature.test.jdk.reflection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Archforce
 * @since 2023/4/7 15:55
 */
@Slf4j
public class ThreadTest {

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
}
