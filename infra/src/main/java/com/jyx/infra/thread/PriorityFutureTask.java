package com.jyx.infra.thread;

/**
 * @author Archforce
 * @since 2023/8/1 16:36
 */
public class PriorityFutureTask<V> extends AbstractPriorityTask<V> {
    public PriorityFutureTask(PriorityCallable<V> priorityCallable) {
        super(priorityCallable.callable, priorityCallable.priority);
    }

    public PriorityFutureTask(PriorityRunnable priorityRunnable, V result) {
        super(priorityRunnable.runnable, result, priorityRunnable.priority);
    }

}
