package com.jyx.feature.test.jdk.concurrent.pool;

import java.util.concurrent.FutureTask;

/**
 * @author Archforce
 * @since 2023/8/1 16:36
 */
public class PriorityFutureTask<V> extends FutureTask<V> implements Comparable<PriorityFutureTask<V>> {

    private final Comparable priorityTask;

    public PriorityFutureTask(PriorityCallable<V> priorityCallable) {
        super(priorityCallable.getCallable());
        this.priorityTask = priorityCallable;
    }

    public PriorityFutureTask(PriorityRunnable priorityRunnable, V result) {
        super(priorityRunnable.getRun(), result);
        this.priorityTask = priorityRunnable;
    }

    @Override
    public int compareTo(PriorityFutureTask<V> otherTask) {
        return priorityTask.compareTo(otherTask.priorityTask);
    }
}
