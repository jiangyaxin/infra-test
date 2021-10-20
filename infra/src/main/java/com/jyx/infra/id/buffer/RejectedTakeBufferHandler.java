package com.jyx.infra.id.buffer;

/**
 * @author JYX
 * @since 2021/10/20 0:05
 */
@FunctionalInterface
public interface RejectedTakeBufferHandler {

    /**
     * 拒绝take
     * @param ringBuffer ringBuffer
     */
    void rejectTakeBuffer(RingBuffer ringBuffer);
}
