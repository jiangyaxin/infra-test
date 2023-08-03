package com.jyx.infra.thread;

import static com.jyx.infra.thread.PriorityThreadPoolExecutor.BASE_PRIORITY;

/**
 * @author Archforce
 * @since 2023/8/1 16:20
 */
public class PriorityRunnable extends AbstractPriorityTask<Void> implements Runnable {


    public PriorityRunnable(Runnable runnable) {
        super(runnable, BASE_PRIORITY);
    }

    public PriorityRunnable(Runnable runnable, int priority) {
        super(runnable, priority);
    }

    @Override
    public void run() {
        super.runnable.run();
    }
}
