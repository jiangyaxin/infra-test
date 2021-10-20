package com.jyx.infra.atomic;

import java.util.concurrent.atomic.AtomicLong;

/**
 *  解决伪共享问题(FalseSharing problem)
 *
 *  CPU 缓存行(cache line)一般是 64 bytes,填充对象大于等于64字节，使一个对象占用一个缓存换，避免L3缓存竞争
 *
 *  64 bytes = 8 bytes (object reference) + 6 * 8 bytes (padded long) + 8 bytes (a long value)
 *
 * @author JYX
 * @since 2021/10/19 22:11
 */
public class PaddingAtomicLong extends AtomicLong {

    /** Padding 6 long (48 bytes) */
    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public PaddingAtomicLong() {
        super();
    }

    public PaddingAtomicLong(long initialValue) {
        super(initialValue);
    }

    /**
     * 防止虚拟机优化对象，将属性删除
     */
    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
}
