package com.jyx.infra.id.buffer;

import com.jyx.infra.asserts.Asserts;
import com.jyx.infra.atomic.PaddingAtomicLong;
import com.jyx.infra.id.DefaultSnowflakeIdAllocator;
import com.jyx.infra.log.Logs;
import com.jyx.infra.thread.NamingThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jyx.infra.datetime.DateTimeConstant.ZONE_DEFAULT;

/**
 * 异步补充数据
 *
 * @author JYX
 * @since 2021/10/20 0:23
 */
@Slf4j
public class BufferPaddingExecutor {

    private static final String WORKER_NAME = "RingBuffer-Padding-Worker";

    /**
     * 是否正在填充数据，放在重复运行
     */
    private final AtomicBoolean running;
    /**
     * 上次填充数据的时间
     */
    private final PaddingAtomicLong lastSecond;
    /**
     * 数据
     */
    private final RingBuffer ringBuffer;
    /**
     * id生成器
     */
    private final BufferedIdProvider idProvider;

    /**
     * 填充线程池
     */
    private final ExecutorService idPadExecutor;

    public BufferPaddingExecutor(RingBuffer ringBuffer, BufferedIdProvider idProvider) {
        this.running = new AtomicBoolean(false);

        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        Asserts.isTrue(DefaultSnowflakeIdAllocator.START_LOCAL_DATE_TIME.isBefore(nowLocalDateTime), "system time before 2021-10-19");

        this.lastSecond = new PaddingAtomicLong(nowLocalDateTime.atZone(ZONE_DEFAULT).toEpochSecond());
        this.ringBuffer = ringBuffer;
        this.idProvider = idProvider;

        // 初始化线程池
        int cores = Runtime.getRuntime().availableProcessors();
        this.idPadExecutor = new ThreadPoolExecutor(cores + 1, cores * 2,
                1L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(1024),
                new NamingThreadFactory(WORKER_NAME));
    }

    public void asyncPadding() {
        idPadExecutor.submit(this::paddingId);
    }

    /**
     * 填充数据，使tail追上cursor
     */
    public void paddingId() {
        Logs.debug(log, "Ready to padding buffer lastSecond:{}. {}", lastSecond.get(), ringBuffer);

        // 一个时间只有一个任务在执行
        if (!running.compareAndSet(false, true)) {
            Logs.debug(log, "Padding buffer is still running. {}", ringBuffer);
            return;
        }

        try {
            paddingIdInternal();
            Logs.debug(log, "End to padding buffer lastSecond:{}. {}", lastSecond.get(), ringBuffer);
        } catch (Exception ex) {
            Logs.error(log, "Padding id failed", ex);
        } finally {
            running.set(false);
        }
    }

    private void paddingIdInternal() {
        // 填充数据
        boolean isFullRingBuffer = false;
        while (!isFullRingBuffer) {
            List<Long> idList = idProvider.provide(lastSecond.incrementAndGet());
            for (Long id : idList) {
                isFullRingBuffer = !ringBuffer.put(id);
                if (isFullRingBuffer) {
                    break;
                }
            }
        }
    }

    public void shutdown() {
        if (!idPadExecutor.isShutdown()) {
            idPadExecutor.shutdownNow();
        }
    }
}
