package com.jyx.experience.entity.id;

import com.jyx.experience.entity.id.buffer.BufferPaddingExecutor;
import com.jyx.experience.entity.id.buffer.RingBuffer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JYX
 * @since 2021/10/20 1:07
 */
@Slf4j
public class CachedSnowflakeIdGenerator extends DefaultSnowflakeIdGenerator{

    private static final int DEFAULT_BOOST_POWER = 3;

    private RingBuffer ringBuffer;

    public CachedSnowflakeIdGenerator(SnowflakeIdFormatter fmt, int dataCenterId, int workerId){
        super(fmt,dataCenterId,workerId);
        initRingBuffer();
    }

    private void initRingBuffer() {
        // 借用未来的时间，maxSequence未消耗完已经在生产下一秒数据
        int bufferSize = ((int) fmt.maxSequence+ 1) << DEFAULT_BOOST_POWER;
        int paddingFactor = RingBuffer.DEFAULT_PADDING_PERCENT;
        this.ringBuffer = new RingBuffer(bufferSize, paddingFactor);
        BufferPaddingExecutor bufferPaddingExecutor = new BufferPaddingExecutor(ringBuffer, this::nextIdsForOneSecond);
        ringBuffer.setBufferPaddingExecutor(bufferPaddingExecutor);
        log.debug("Initialized ring buffer size:{}, paddingFactor:{}", bufferSize, paddingFactor);

        // 启动时填充数据
        bufferPaddingExecutor.paddingBuffer();
    }

    protected List<Long> nextIdsForOneSecond(long currentSecond) {
        // 数组长度 = max sequence + 1
        int listSize = (int) fmt.maxSequence + 1;
        List<Long> idList = new ArrayList<>(listSize);

        // 取第一秒，然后依次递增至最大值
        // 当前时间不能小于上一秒时间
        long firstIdInSecond =  fmt.toId(currentSecond - startSecond, dataCenterId, workerId, 0L);
        for (int offset = 0; offset < listSize; offset++) {
            idList.add(firstIdInSecond + offset);
        }

        return idList;
    }


    @Override
    public Long getId() {
        return ringBuffer.take();
    }
}
