package com.jyx.infra.id;

import com.jyx.infra.asserts.Asserts;
import com.jyx.infra.id.buffer.BufferPaddingExecutor;
import com.jyx.infra.id.buffer.RingBuffer;
import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 每次重新启动需要更换workid，因为 CachedSnowflakeIdAllocator 会借用未来时间
 *
 * @author jiangyaxin
 * @since 2021/10/20 1:07
 */
@Slf4j
public class CachedSnowflakeIdAllocator extends DefaultSnowflakeIdAllocator {

    private static final int DEFAULT_BOOST_POWER = 3;

    private RingBuffer ringBuffer;

    private BufferPaddingExecutor bufferPaddingExecutor;

    public CachedSnowflakeIdAllocator(SnowflakeIdFormatter fmt, int dataCenterId, int workerId) {
        super(fmt, dataCenterId, workerId);
        initRingBuffer();
    }

    private void initRingBuffer() {
        // 借用未来的时间，maxSequence未消耗完已经在生产下一秒数据
        int bufferSize = ((int) fmt.maxSequence + 1) << DEFAULT_BOOST_POWER;
        int paddingFactor = RingBuffer.DEFAULT_PADDING_PERCENT;
        this.ringBuffer = new RingBuffer(bufferSize, paddingFactor);
        this.bufferPaddingExecutor = new BufferPaddingExecutor(ringBuffer, this::nextIdsForOneSecond);
        ringBuffer.setBufferPaddingExecutor(bufferPaddingExecutor);

        Logs.debug(log, "Initialized ring buffer size:{}, paddingFactor:{}", bufferSize, paddingFactor);

        // 启动时填充数据
        bufferPaddingExecutor.paddingId();
    }

    protected List<Long> nextIdsForOneSecond(long currentSecond) {
        // 秒数达到最大值
        Asserts.isTrue(currentSecond - startSecond <= fmt.maxTimestamp,
                () -> String.format("Timestamp bits is exhausted. Refusing id generate. Now: %s", currentSecond));

        // 数组长度 = max sequence + 1
        int listSize = (int) fmt.maxSequence + 1;
        List<Long> idList = new ArrayList<>(listSize);

        // 取第一秒，然后依次递增至最大值
        // 当前时间不能小于上一秒时间
        long firstIdInSecond = fmt.toId(currentSecond - startSecond, dataCenterId, workerId, 0L);
        for (int offset = 0; offset < listSize; offset++) {
            idList.add(firstIdInSecond + offset);
        }

        return idList;
    }

    @Override
    public void close() {
        bufferPaddingExecutor.shutdown();

        Logs.info(log, "CachedSnowflakeIdAllocator stopped");
    }

    @Override
    public Long getId() {
        return ringBuffer.take();
    }
}
