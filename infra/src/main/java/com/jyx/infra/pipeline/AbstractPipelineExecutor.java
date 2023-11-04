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
public abstract class AbstractPipelineExecutor<T> implements PipelineExecutor<T> {

    protected final String name;

    protected final LinkedList<StageDefinition<T>> stageDefinitionChain;

    protected PipelineState pipelineState;

    protected long stopTimeout;

    protected TimeUnit stopTimeUnit;

    public AbstractPipelineExecutor(String name, long stopTimeout, TimeUnit stopTimeUnit) {
        this.name = name;
        this.stageDefinitionChain = new LinkedList<>();
        this.pipelineState = PipelineState.READY;
        this.stopTimeout = stopTimeout;
        this.stopTimeUnit = stopTimeUnit;
    }

    protected abstract void start0();

    protected abstract void stop0(long timeout, TimeUnit timeUnit);

    protected void preSubmit(T data) {

    }

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
    public void stop() {
        switch (pipelineState) {
            case READY:
                Logs.warn(log, "Pipeline not started,not need to stop: {}", name);
                break;
            case RUNNING:
                pipelineState = pipelineState.nextState();
                stop0(stopTimeout, stopTimeUnit);
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
    public void addStage(Iterable<StageDefinition<T>> stageIterable) {
        switch (pipelineState) {
            case READY:
                if (stageIterable == null) {
                    break;
                }
                stageIterable.forEach(stageDefinitionChain::offer);
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
                preSubmit(data);
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
                preSubmit(data);
                return trySubmit0(data);
            case STOP:
                throw new PipelineException(String.format("Pipeline already stopped,cannot trySubmit data: %s , %s", name, data));
            default:
                throw new PipelineException(String.format("Error pipeline state,cannot trySubmit data: %s , %s", pipelineState.name(), data));
        }
    }
}
