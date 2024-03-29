package com.jyx.infra.pipeline.disruptor;

import com.jyx.infra.pipeline.PipelineException;
import com.lmax.disruptor.*;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangyaxin
 * @since 2023/11/1 11:37
 */
public enum WaitMode {
    BLOCKING("使用锁进行数据监控和线程等待、唤醒，最节省CPU，但由于需要线程切换在高并发场景下性能最糟糕"),
    TIMEOUT_BLOCKING("基于 BlockingWaitStrategy 的带超时策略，超时后会抛出异常"),
    SLEEPING("自旋等待，如果不成功会调用yield让出CPU,可能会产生较高的平均延时，对延时要求不是特别高的场合，对生产者线程影响最小，典型场景就是异步日志"),
    YIELDING("用于低延时环境，内部是一个 yield 的死循环，会消耗大量的CPU资源，相当于绑定该核心，最好有多于该消费者线程数量的核心数量"),
    BUSY_SPIN("相比 YieldingWaitStrategy 更加疯狂，不会让出CPU，吃掉所有的CPU资源，所以物理核必须要多于消费者数目,即使存在逻辑核也会影响无法工作"),
    LITE_BLOCKING("基于 BlockingWaitStrategy 的轻量级等待策略，在没有锁竞争的时候会省去唤醒操作"),
    LITE_TIMEOUT_BLOCKING("基于 BlockingWaitStrategy 的轻量级带超时策略，在没有锁竞争的时候会省去唤醒操作，超时后会抛出异常"),
    PHASED_BACKOFF("四阶段策略，先固定自旋次数，再自旋一段时间，再yield一段时间，最后使用其他策略进行兜底");

    private final String comment;

    WaitMode(String comment) {
        this.comment = comment;
    }


    public static WaitStrategy buildWaitStrategy(WaitStrategyProperties waitStrategyProperties) {
        WaitMode waitMode = waitStrategyProperties.getWaitMode();
        Long spinTimeout = waitStrategyProperties.getSpinTimeout();
        Long timeout = waitStrategyProperties.getTimeout();
        TimeUnit timeUnit = waitStrategyProperties.getTimeUnit();
        WaitMode fallbackWaitMode = waitStrategyProperties.getFallbackWaitMode();
        switch (waitMode) {
            case BLOCKING:
                return new BlockingWaitStrategy();
            case TIMEOUT_BLOCKING:
                checkTimeout(waitMode, timeout);
                checkTimeUnit(waitMode, timeUnit);
                return new TimeoutBlockingWaitStrategy(timeout, timeUnit);
            case SLEEPING:
                return new SleepingWaitStrategy();
            case YIELDING:
                return new YieldingWaitStrategy();
            case BUSY_SPIN:
                return new BusySpinWaitStrategy();
            case LITE_BLOCKING:
                return new LiteBlockingWaitStrategy();
            case LITE_TIMEOUT_BLOCKING:
                checkTimeout(waitMode, timeout);
                checkTimeUnit(waitMode, timeUnit);
                return new LiteTimeoutBlockingWaitStrategy(timeout, timeUnit);
            case PHASED_BACKOFF:
                checkSpinTimeout(waitMode, spinTimeout);
                checkTimeout(waitMode, timeout);
                checkTimeUnit(waitMode, timeUnit);
                checkFallbackWaitMode(waitMode, fallbackWaitMode);
                switch (fallbackWaitMode) {
                    case BLOCKING:
                        return PhasedBackoffWaitStrategy.withLock(spinTimeout,timeout,timeUnit);
                    case LITE_BLOCKING:
                        return PhasedBackoffWaitStrategy.withLiteLock(spinTimeout,timeout,timeUnit);
                    case SLEEPING:
                        return PhasedBackoffWaitStrategy.withSleep(spinTimeout,timeout,timeUnit);
                    default:
                        throw new PipelineException(String.format("%s mode not support,except BLOCKING,LITE_BLOCKING,SLEEPING", waitMode.name()));
                }
            default:
                throw new PipelineException(String.format("Error wait mode:%s", waitMode.name()));
        }
    }

    private static void checkSpinTimeout(WaitMode waitMode, Long spinTimeout) {
        if (spinTimeout == null) {
            throw new PipelineException(String.format("SpinTimeout is null at %s mode.", waitMode.name()));
        }
    }

    private static void checkTimeout(WaitMode waitMode, Long timeout) {
        if (timeout == null) {
            throw new PipelineException(String.format("Timeout is null at %s mode.", waitMode.name()));
        }
    }

    private static void checkTimeUnit(WaitMode waitMode, TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new PipelineException(String.format("TimeUnit is null at %s mode.", waitMode.name()));
        }
    }

    private static void checkFallbackWaitMode(WaitMode waitMode, WaitMode fallbackWaitMode) {
        if (fallbackWaitMode == null) {
            throw new PipelineException(String.format("FallbackWaitMode is null at %s mode.", waitMode.name()));
        }
    }

}
