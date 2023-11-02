package com.jyx.infra.pipeline;

import java.util.function.Consumer;

/**
 * @author Archforce
 * @since 2023/11/1 10:33
 */
public interface StageDefinition<T> {

    String name();

    int parallel();

    Consumer<PipelineEvent<T>> consumer();

    StageDefinition<T> fork();
}
