package com.jyx.infra.thread;

import java.util.concurrent.Callable;

import static com.jyx.infra.thread.PriorityThreadPoolExecutor.BASE_PRIORITY;

/**
 * @author jiangyaxin
 * @since 2023/8/1 16:20
 */
public class PriorityCallable<V> extends AbstractPriorityTask<V> implements Callable<V> {


    public PriorityCallable(Callable<V> callable) {
        super(callable, BASE_PRIORITY);
    }

    public PriorityCallable(Callable<V> callable, int priority) {
        super(callable, priority);
    }

    @Override
    public V call() throws Exception {
        return super.callable.call();
    }

}
