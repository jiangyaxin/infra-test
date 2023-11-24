package com.jyx.infra.thread;

import java.util.concurrent.*;

/**
 * @author jiangyaxin
 * @since 2023/8/1 16:31
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    public final static int BASE_PRIORITY = Integer.MIN_VALUE;

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int queueSize) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<>(queueSize));
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int queueSize, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<>(queueSize), threadFactory);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int queueSize, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<>(queueSize), handler);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int queueSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<>(queueSize), threadFactory, handler);
    }

    @Override
    protected <T> PriorityFutureTask<T> newTaskFor(Runnable runnable, T value) {
        return new PriorityFutureTask<>(new PriorityRunnable(runnable), value);
    }

    protected <T> PriorityFutureTask<T> newTaskFor(Runnable runnable, T value, int priority) {
        return new PriorityFutureTask<>(new PriorityRunnable(runnable, priority), value);
    }

    @Override
    protected <T> PriorityFutureTask<T> newTaskFor(Callable<T> callable) {
        return new PriorityFutureTask<>(new PriorityCallable(callable));
    }

    protected <T> PriorityFutureTask<T> newTaskFor(Callable<T> callable, int priority) {
        return new PriorityFutureTask<>(new PriorityCallable(callable, priority));
    }

    @Override
    public void execute(Runnable command) {
        super.execute(new PriorityRunnable(command));
    }


    public void execute(Runnable command, int priority) {
        super.execute(new PriorityRunnable(command, priority));
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        PriorityFutureTask<Void> ftask = newTaskFor(task, null);
        super.execute(ftask);
        return ftask;
    }


    public Future<?> submit(Runnable task, int priority) {
        if (task == null) {
            throw new NullPointerException();
        }
        PriorityFutureTask<Void> ftask = newTaskFor(task, null, priority);
        super.execute(ftask);
        return ftask;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (task == null) {
            throw new NullPointerException();
        }
        PriorityFutureTask<T> ftask = newTaskFor(task, result);
        super.execute(ftask);
        return ftask;
    }

    public <T> Future<T> submit(Runnable task, T result, int priority) {
        if (task == null) {
            throw new NullPointerException();
        }
        PriorityFutureTask<T> ftask = newTaskFor(task, result, priority);
        super.execute(ftask);
        return ftask;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        PriorityFutureTask<T> ftask = newTaskFor(task);
        super.execute(ftask);
        return ftask;
    }

    public <T> Future<T> submit(Callable<T> task, int priority) {
        if (task == null) {
            throw new NullPointerException();
        }
        PriorityFutureTask<T> ftask = newTaskFor(task, priority);
        super.execute(ftask);
        return ftask;
    }
}
