package com.jyx.infra.pipeline.disruptor;

import com.jyx.infra.log.Logs;
import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.PipelineExecutor;
import com.jyx.infra.pipeline.StageDefinition;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @author Archforce
 * @since 2023/11/1 15:24
 */
@Slf4j
public class StageHandler<T> implements WorkHandler<PipelineEvent<T>> {

    private final PipelineExecutor<T> pipelineExecutor;

    private final StageDefinition<T> stageDefinition;

    public StageHandler(PipelineExecutor<T> pipelineExecutor, StageDefinition<T> stageDefinition) {
        this.pipelineExecutor = pipelineExecutor;
        this.stageDefinition = stageDefinition;
    }

    @Override
    public void onEvent(PipelineEvent<T> event) {
        CompletableFuture<Void> future = event.future();
        try {
            if (future.isDone()) {
                return;
            }
            event.stage(stageDefinition);
            stageDefinition.consumer().accept(event);
        } catch (Exception e) {
            Logs.error(log, "Pipeline execute error at {}#{}", pipelineExecutor.name(), stageDefinition.name(), e);
            future.completeExceptionally(e);
        }
    }
}
