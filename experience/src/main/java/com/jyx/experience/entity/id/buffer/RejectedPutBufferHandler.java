package com.jyx.experience.entity.id.buffer;

/**
 * @author JYX
 * @since 2021/10/19 23:58
 */
@FunctionalInterface
public interface RejectedPutBufferHandler {

    /**
     * 拒绝put
     * @param ringBuffer ringBuffer
     * @param id id
     */
    void rejectPutBuffer(RingBuffer ringBuffer, long id);
}
