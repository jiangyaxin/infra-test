package com.jyx.infra.pipeline.disruptor;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangyaxin
 * @since 2023/11/1 14:33
 */
@Data
public class WaitStrategyProperties {

    private WaitMode waitMode;

    private Long spinTimeout;

    private Long timeout;

    private TimeUnit timeUnit;

    private WaitMode fallbackWaitMode;

    public WaitStrategyProperties() {
    }

    public WaitStrategyProperties(WaitMode waitMode) {
        this.waitMode = waitMode;
    }

    public WaitStrategyProperties(WaitMode waitMode, Long timeout, TimeUnit timeUnit) {
        this.waitMode = waitMode;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public WaitStrategyProperties(WaitMode waitMode, Long spinTimeout, Long timeout, TimeUnit timeUnit, WaitMode fallbackWaitMode) {
        this.waitMode = waitMode;
        this.spinTimeout = spinTimeout;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.fallbackWaitMode = fallbackWaitMode;
    }
}
