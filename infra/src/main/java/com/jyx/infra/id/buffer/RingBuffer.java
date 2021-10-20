package com.jyx.infra.id.buffer;

import com.jyx.infra.atomic.PaddingAtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author JYX
 * @since 2021/10/19 23:36
 */
@Slf4j
public class RingBuffer {

    private static final int START_POINT = -1;
    private static final long CAN_PUT_FLAG = 0L;
    private static final long CAN_TAKE_FLAG = 1L;
    public static final int DEFAULT_PADDING_PERCENT = 50;

    /**
     * 缓存长度
     */
    private final int bufferSize;
    /**
     * 长度-1 ，用来取模得到数组下标
     */
    private final long indexMask;
    /**
     * 缓存数组
     */
    private final long[] slots;
    /**
     * 对应缓存数组是否可以取出值，或者放入值
     */
    private final PaddingAtomicLong[] flags;
    /**
     * 补充数据的阀值，默认 50% * bufferSize
     */
    private final int paddingThreshold;
    /**
     * 生产者所在位置
     */
    private final AtomicLong tail = new PaddingAtomicLong(START_POINT);
    /**
     * 消费者所在位置
     */
    private final AtomicLong cursor = new PaddingAtomicLong(START_POINT);

    /**
     * 拒绝 put/take 策略
     */
    private final RejectedPutBufferHandler rejectedPutHandler = this::discardPutBuffer;
    private final RejectedTakeBufferHandler rejectedTakeHandler = this::exceptionRejectedTakeBuffer;

    private BufferPaddingExecutor bufferPaddingExecutor;

    public RingBuffer(int bufferSize, int paddingFactor) {
        // bufferSize 需要为2的倍数
        Assert.isTrue(bufferSize > 0L, "RingBuffer size must be positive");
        Assert.isTrue(Integer.bitCount(bufferSize) == 1, "RingBuffer size must be a power of 2");
        Assert.isTrue(paddingFactor > 0 && paddingFactor < 100, "RingBuffer size must be positive");

        this.bufferSize = bufferSize;
        this.indexMask = bufferSize - 1;
        this.slots = new long[bufferSize];
        //系统启动时所有slot都为 CAN_PUT_FLAG
        this.flags = initFlags(bufferSize);

        this.paddingThreshold = bufferSize * paddingFactor / 100;
    }

    private PaddingAtomicLong[] initFlags(int bufferSize) {
        PaddingAtomicLong[] flags = new PaddingAtomicLong[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            flags[i] = new PaddingAtomicLong(CAN_PUT_FLAG);
        }
        return flags;
    }

    /**
     *
     * @param id id
     * @return false 表示 缓存区已经放满
     */
    public synchronized boolean put(long id) {
        long currentTail = tail.get();
        long currentCursor = cursor.get();

        // 生产者 - 消费者 = 缓存最大长度表示已经放满
        long distance = currentTail - (currentCursor == START_POINT ? 0 : currentCursor);
        if (distance == bufferSize - 1) {
            rejectedPutHandler.rejectPutBuffer(this, id);
            return false;
        }

        // 1. 检查下一个位置是否可以放
        int nextTailIndex = calSlotIndex(currentTail + 1);
        if (flags[nextTailIndex].get() != CAN_PUT_FLAG) {
            rejectedPutHandler.rejectPutBuffer(this, id);
            return false;
        }

        // 2. 将id放入slot
        // 3. 将flag置为CAN_TAKE_FLAG
        // 4. 移动tail位置
        slots[nextTailIndex] = id;
        flags[nextTailIndex].set(CAN_TAKE_FLAG);
        // 在tail.incrementAndGet()之前id不能被消费
        tail.incrementAndGet();

        return true;
    }

    /**
     * 并发获取id
     * @return id
     */
    public long take() {
        // 获取下一个消费者序列号cursor
        long currentCursor = cursor.get();
        // 一次take获取一个不会重复，和put相反，put最后才改变tail，take一开始就改变cursor
        long nextCursor = cursor.updateAndGet(old -> old == tail.get() ? old : old + 1);

        // 下一个序列号大于等于当前序列号
        Assert.isTrue(nextCursor >= currentCursor, "Cursor can't move back");

        // 触发异步填充数据
        long currentTail = tail.get();
        if (currentTail - nextCursor < paddingThreshold) {
            log.debug("Reach the padding threshold:{}. tail:{}, cursor:{}",
                    paddingThreshold,
                    currentTail,
                    nextCursor);
            bufferPaddingExecutor.asyncPadding();
        }

        // cursor==tail 缓存id消费完
        if (nextCursor == currentCursor) {
            rejectedTakeHandler.rejectTakeBuffer(this);
        }

        // 1.检查下一个cursor是否CAN_TAKE_FLAG
        int nextCursorIndex = calSlotIndex(nextCursor);
        Assert.isTrue(flags[nextCursorIndex].get() == CAN_TAKE_FLAG, "Cursor not in can take status");

        // 2. 获取id
        // 3. 将对应的flags置为CAN_PUT_FLAG
        long id = slots[nextCursorIndex];
        flags[nextCursorIndex].set(CAN_PUT_FLAG);

        // 2,3步不能交换，否则生产者有可能将该id覆盖。
        return id;
    }

    /**
     * 获取cursor和tail的数组索引
     *
     * @return 数组索引
     */
    private int calSlotIndex(long sequence) {
        return (int) (sequence & indexMask);
    }

    protected void discardPutBuffer(RingBuffer ringBuffer, long id) {
        log.debug("Rejected putting buffer for id:{}. {}", id, ringBuffer);
    }

    protected void exceptionRejectedTakeBuffer(RingBuffer ringBuffer) {
        throw new RuntimeException("Rejected take buffer. " + ringBuffer);
    }

    public void setBufferPaddingExecutor(BufferPaddingExecutor bufferPaddingExecutor) {
        Assert.notNull(bufferPaddingExecutor,"bufferPaddingExecutor is not null");
        this.bufferPaddingExecutor = bufferPaddingExecutor;
    }

    @Override
    public String toString() {

        return "RingBuffer [bufferSize=" + bufferSize +
                ", tail=" + tail +
                ", cursor=" + cursor +
                ", paddingThreshold=" + paddingThreshold + "]";
    }

}
