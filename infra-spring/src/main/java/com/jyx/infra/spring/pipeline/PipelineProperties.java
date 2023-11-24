package com.jyx.infra.spring.pipeline;

import com.jyx.infra.pipeline.disruptor.WaitStrategyProperties;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangyaxin
 * @since 2023/11/3 15:25
 */
@Data
public class PipelineProperties {

    private Boolean enable;

    private Integer bufferSize;

    private Long stopTimeout;

    private TimeUnit stopTimeUnit;

    private WaitStrategyProperties waitStrategy;

    private Map<String, StageProperties> stageConfig;
}
