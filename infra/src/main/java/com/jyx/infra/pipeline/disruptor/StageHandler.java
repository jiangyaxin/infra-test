package com.jyx.infra.pipeline.disruptor;

import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.Stage;
import com.lmax.disruptor.WorkHandler;

/**
 * @author Archforce
 * @since 2023/11/1 15:24
 */
public class StageHandler<T> implements WorkHandler<PipelineEvent<T>> {

    private final Stage<T> stage;

    public StageHandler(Stage<T> stage) {
        this.stage = stage;
    }

    @Override
    public void onEvent(PipelineEvent<T> event) throws Exception {
        event.stage(stage);
        stage.consumer().accept(event);
    }
}
