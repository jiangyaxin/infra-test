package com.jyx.feature.test.jdk.concurrent.pool;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

import static com.jyx.feature.test.jdk.concurrent.pool.PriorityThreadPoolExecutor.BASE_PRIORITY;

/**
 * @author Archforce
 * @since 2023/8/1 16:20
 */
public class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {

    private final static AtomicLong sequencer = new AtomicLong();

    private final long sequence;
    @Getter
    private final Runnable run;
    private final int priority;

    public PriorityRunnable(Runnable run) {
        this.sequence = sequencer.getAndIncrement();
        this.run = run;
        this.priority = BASE_PRIORITY;
    }

    public PriorityRunnable(Runnable run, int priority) {
        this.sequence = sequencer.getAndIncrement();
        this.run = run;
        this.priority = priority;
    }

    /**
     * 1. priority 相同，先入先出
     * 2. priority 不同，越大越先
     */
    @Override
    public int compareTo(PriorityRunnable other) {
        int res = 0;
        if (this.priority == other.priority) {
            if (other.run != this.run) {
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
    public void run() {
        this.run.run();
    }
}
