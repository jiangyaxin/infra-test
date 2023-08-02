package com.jyx.feature.test.jdk.concurrent.pool;

import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import static com.jyx.feature.test.jdk.concurrent.pool.PriorityThreadPoolExecutor.BASE_PRIORITY;

/**
 * @author Archforce
 * @since 2023/8/1 16:20
 */
public class PriorityCallable<V> implements Callable<V>, Comparable<PriorityCallable<V>> {

    private final static AtomicLong sequencer = new AtomicLong();

    private final long sequence;

    @Getter
    private final Callable<V> callable;
    private final int priority;

    public PriorityCallable(Callable<V> callable) {
        this.sequence = sequencer.getAndIncrement();
        this.callable = callable;
        this.priority = BASE_PRIORITY;
    }

    public PriorityCallable(Callable<V> callable, int priority) {
        this.sequence = sequencer.getAndIncrement();
        this.callable = callable;
        this.priority = priority;
    }

    /**
     * 1. priority 相同，先入先出
     * 2. priority 不同，越大越先
     */
    @Override
    public int compareTo(PriorityCallable<V> other) {
        int res = 0;
        if (this.priority == other.priority) {
            if (other.callable != this.callable) {
                // ASC
                res = (sequence < other.sequence ? -1 : 1);
            }
        } else {
            // DESC
            res = this.priority > other.priority ? -1 : 1;
        }
        return res;
    }

    @Override
    public V call() throws Exception {
        return callable.call();
    }

}
