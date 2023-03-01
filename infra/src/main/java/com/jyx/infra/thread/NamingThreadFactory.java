package com.jyx.infra.thread;

import com.jyx.infra.asserts.Asserts;
import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author JYX
 * @since 2021/10/20 0:35
 */
@Slf4j
public class NamingThreadFactory implements ThreadFactory {

    /**
     * 线程名称前缀
     */
    private final String prefix;
    /**
     * 是否守护线程
     */
    private final boolean daemon;
    /**
     * UncaughtExceptionHandler
     */
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    /**
     * 线程编号
     */
    private final AtomicLong sequence;

    public NamingThreadFactory(String prefix) {
        this(prefix, false, null);
    }

    public NamingThreadFactory(String prefix, boolean daemon) {
        this(prefix, daemon, null);
    }

    public NamingThreadFactory(String prefix, boolean daemon, Thread.UncaughtExceptionHandler handler) {
        Asserts.hasText(prefix, "prefix is not null ");

        this.prefix = prefix;
        this.daemon = daemon;
        this.uncaughtExceptionHandler = handler;
        this.sequence = new AtomicLong(0);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(this.daemon);
        thread.setName(prefix + "-" + sequence.incrementAndGet());
        thread.setUncaughtExceptionHandler(Objects.requireNonNullElseGet(this.uncaughtExceptionHandler, () ->
                (t, e) -> Logs.error(log, "unhandled exception in thread: " + t.getId() + ":" + t.getName(), e)
        ));

        return thread;
    }

    public String getPrefix() {
        return prefix;
    }
}
