package com.jyx.infra.pipeline;

import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @author Archforce
 * @since 2023/11/1 10:59
 */
@Slf4j
public abstract class AbstractPipeline<T> implements Pipeline<T> {

    protected final String name;

    protected final LinkedList<Stage<T>> stageChain;

    protected PipelineState pipelineState;

    public AbstractPipeline(String name) {
        this.name = name;
        this.stageChain = new LinkedList<>();
        this.pipelineState = PipelineState.READY;
    }

    protected abstract void start0();

    protected abstract void stop0(long timeout, TimeUnit timeUnit);

    protected abstract boolean submit0(T data);

    protected abstract boolean trySubmit0(T data);

    @Override
    public String name() {
        return name;
    }

    @Override
    public PipelineState state() {
        return pipelineState;
    }

    @Override
    public void start() {
        switch (pipelineState) {
            case READY:
                pipelineState = pipelineState.nextState();
                start0();
                Logs.info(log, "Pipeline is running:{}", name);
                break;
            case RUNNING:
                Logs.warn(log, "Pipeline already started,cannot repeat start: {}", name);
                break;
            case STOP:
                Logs.warn(log, "Pipeline already stopped,cannot start: {}", name);
                break;
            default:
                throw new PipelineException(String.format("Error pipeline state,cannot start: %s", pipelineState.name()));
        }
    }

    @Override
    public void stop(long timeout, TimeUnit timeUnit) {
        switch (pipelineState) {
            case READY:
                Logs.warn(log, "Pipeline not started,not need to stop: {}", name);
                break;
            case RUNNING:
                pipelineState = pipelineState.nextState();
                stop0(timeout, timeUnit);
                Logs.info(log, "Pipeline is stopped:{}", name);
                break;
            case STOP:
                Logs.warn(log, "Pipeline already stopped,cannot repeat stop: {}", name);
                break;
            default:
                throw new PipelineException(String.format("Error pipeline state,cannot stop: %s", pipelineState.name()));
        }
    }

    @Override
    public void addStage(Iterable<Stage<T>> stageIterable) {
        switch (pipelineState) {
            case READY:
                stageIterable.forEach(stageChain::offer);
                break;
            case RUNNING:
                throw new PipelineException(String.format("Pipeline already started,cannot add stage: %s", name));
            case STOP:
                throw new PipelineException(String.format("Pipeline already stopped,cannot add stage: %s", name));
            default:
                throw new PipelineException(String.format("Error pipeline state,cannot add stage: %s", pipelineState.name()));
        }
    }

    @Override
    public boolean submit(T data) {
        switch (pipelineState) {
            case READY:
                throw new PipelineException(String.format("Pipeline not started,cannot submit data: %s , %s", name, data));
            case RUNNING:
                return submit0(data);
            case STOP:
                throw new PipelineException(String.format("Pipeline already stopped,cannot submit data: %s , %s", name, data));
            default:
                throw new PipelineException(String.format("Error pipeline state,cannot submit data: %s , %s", pipelineState.name(), data));
        }
    }

    @Override
    public boolean trySubmit(T data) {
        switch (pipelineState) {
            case READY:
                throw new PipelineException(String.format("Pipeline not started,cannot trySubmit data: %s , %s", name, data));
            case RUNNING:
                return trySubmit0(data);
            case STOP:
                throw new PipelineException(String.format("Pipeline already stopped,cannot trySubmit data: %s , %s", name, data));
            default:
                throw new PipelineException(String.format("Error pipeline state,cannot trySubmit data: %s , %s", pipelineState.name(), data));
        }
    }
}
