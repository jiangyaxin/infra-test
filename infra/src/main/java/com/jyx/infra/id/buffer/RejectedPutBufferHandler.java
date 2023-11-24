package com.jyx.infra.id.buffer;

/**
 * @author jiangyaxin
 * @since 2021/10/19 23:58
 */
@FunctionalInterface
public interface RejectedPutBufferHandler {

    /**
     * 拒绝put
     *
     * @param ringBuffer ringBuffer
     * @param id         id
     */
    void rejectPutBuffer(RingBuffer ringBuffer, long id);
}
