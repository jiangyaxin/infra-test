package com.jyx.infra.pipeline.disruptor;

import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.StageDefinition;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Archforce
 * @since 2023/11/1 14:51
 */
public class StageDefinitionImpl<T> implements StageDefinition<T> {

    private AtomicInteger index = new AtomicInteger(0);

    private final String name;
    private final int parallel;
    private final Consumer<PipelineEvent<T>> consumer;

    public StageDefinitionImpl(String name, int parallel, Consumer<PipelineEvent<T>> consumer) {
        this.name = name;
        this.parallel = parallel;
        this.consumer = consumer;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int parallel() {
        return parallel;
    }

    public Consumer<PipelineEvent<T>> consumer() {
        return consumer;
    }

    @Override
    public StageDefinition<T> fork() {
        return new StageDefinitionImpl<>(name + "-" + index.incrementAndGet(), parallel, consumer);
    }
}
