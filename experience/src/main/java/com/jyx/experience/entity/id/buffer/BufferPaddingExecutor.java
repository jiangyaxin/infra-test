package com.jyx.experience.entity.id.buffer;

import com.jyx.experience.atomic.PaddingAtomicLong;
import com.jyx.experience.thread.NamingThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jyx.experience.datetime.DateTimeConstant.ZONE_DEFAULT;
import static com.jyx.experience.entity.id.DefaultSnowflakeIdGenerator.START_LOCAL_DATE_TIME;

/**
 * 异步补充数据
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
    private final BufferedIdProvider uidProvider;

    /**
     * 填充线程池
     */
    private final ExecutorService bufferPadExecutors;

    public BufferPaddingExecutor(RingBuffer ringBuffer, BufferedIdProvider idProvider) {
        this.running = new AtomicBoolean(false);

        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        Assert.isTrue(START_LOCAL_DATE_TIME.isBefore(nowLocalDateTime),"system time before 2021-10-19");
        this.lastSecond = new PaddingAtomicLong(nowLocalDateTime.atZone(ZONE_DEFAULT).toEpochSecond());
        this.ringBuffer = ringBuffer;
        this.uidProvider = idProvider;

        // 初始化线程池
        int cores = Runtime.getRuntime().availableProcessors();
        int threadNumber = cores*2;
        this.bufferPadExecutors = new ThreadPoolExecutor(threadNumber, threadNumber,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamingThreadFactory(WORKER_NAME));
    }

    public void asyncPadding() {
        bufferPadExecutors.submit(this::paddingBuffer);
    }

    /**
     * 填充数据，使tail追上cursor
     */
    public void paddingBuffer() {
        log.debug("Ready to padding buffer lastSecond:{}. {}", lastSecond.get(), ringBuffer);

        // 一个时间只有一个任务在执行
        if (!running.compareAndSet(false, true)) {
            log.debug("Padding buffer is still running. {}", ringBuffer);
            return;
        }

        // 填充数据
        boolean isFullRingBuffer = false;
        while (!isFullRingBuffer) {
            List<Long> idList = uidProvider.provide(lastSecond.incrementAndGet());
            for (Long id : idList) {
                isFullRingBuffer = !ringBuffer.put(id);
                if (isFullRingBuffer) {
                    break;
                }
            }
        }

        running.compareAndSet(true, false);
        log.debug("End to padding buffer lastSecond:{}. {}", lastSecond.get(), ringBuffer);
    }
}
