package com.jyx.infra.spring.pipeline;

import com.jyx.infra.pipeline.disruptor.WaitStrategyProperties;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Archforce
 * @since 2023/11/3 15:25
 */
@Data
public class PipelineProperties {

    int bufferSize;

    private Long stopTimeout;

    private TimeUnit stopTimeUnit;

    WaitStrategyProperties waitStrategy;

    Map<String, StageProperties> stageConfig;
}
