package com.jyx.infra.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Archforce
 * @since 2023/8/2 9:42
 */
public abstract class AbstractPriorityTask<V> extends FutureTask<V> implements Comparable<AbstractPriorityTask<V>> {

    protected final static AtomicLong sequencer = new AtomicLong();

    protected final long sequence;

    protected final int priority;

    protected Runnable runnable;

    protected Callable<V> callable;

    public AbstractPriorityTask(Runnable runnable, int priority) {
        this(runnable, null, priority);
    }

    public AbstractPriorityTask(Runnable runnable, V result, int priority) {
        super(runnable, result);
        this.sequence = sequencer.getAndIncrement();
        this.runnable = runnable;
        this.callable = null;
        this.priority = priority;
    }

    public AbstractPriorityTask(Callable<V> callable, int priority) {
        super(callable);
        this.sequence = sequencer.getAndIncrement();
        this.runnable = null;
        this.callable = callable;
        this.priority = priority;
    }

    @Override
    public int compareTo(AbstractPriorityTask<V> other) {
        int res;
        if (this.priority == other.priority) {
            // ASC
            res = (sequence < other.sequence ? -1 : 1);
        } else {
            // DESC
            res = this.priority > other.priority ? -1 : 1;
        }
        return res;
    }
}
