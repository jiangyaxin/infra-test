package com.jyx.infra.pipeline.disruptor;

import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.StageDefinition;
import com.lmax.disruptor.WorkHandler;

/**
 * @author Archforce
 * @since 2023/11/1 15:24
 */
public class StageHandler<T> implements WorkHandler<PipelineEvent<T>> {

    private final StageDefinition<T> stageDefinition;

    public StageHandler(StageDefinition<T> stageDefinition) {
        this.stageDefinition = stageDefinition;
    }

    @Override
    public void onEvent(PipelineEvent<T> event) throws Exception {
        event.stage(stageDefinition);
        stageDefinition.consumer().accept(event);
    }
}
