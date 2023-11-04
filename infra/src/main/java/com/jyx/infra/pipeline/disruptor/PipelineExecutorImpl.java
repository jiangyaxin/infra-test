package com.jyx.infra.pipeline.disruptor;

import com.jyx.infra.log.Logs;
import com.jyx.infra.pipeline.AbstractPipelineExecutor;
import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.PipelineException;
import com.jyx.infra.pipeline.StageDefinition;
import com.jyx.infra.thread.NamingThreadFactory;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Archforce
 * @since 2023/11/1 10:36
 */
@Slf4j
public class PipelineExecutorImpl<T> extends AbstractPipelineExecutor<T> {

    private final Disruptor<PipelineEvent<T>> disruptor;

    private final WorkHandler<PipelineEvent<T>> HEAD_HANDLER = event -> Logs.debug(log, "Start process sequence:{} data:{}", event.sequence(), event.data());

    private final WorkHandler<PipelineEvent<T>> TAIL_HANDLER = event -> {
        Logs.debug(log, "Finish process sequence:{} data:{}", event.sequence(), event.data());
        event.clear();
    };

    private final EventTranslatorOneArg<PipelineEvent<T>, T> EVENT_TRANSLATOR = PipelineEvent::load;

    public PipelineExecutorImpl(String name, int bufferSize, WaitStrategyProperties waitStrategyProperties) {
        this(name, bufferSize, waitStrategyProperties, -1);
    }


    public PipelineExecutorImpl(String name, int bufferSize, WaitStrategyProperties waitStrategyProperties, long stopTimeout) {
        this(name, bufferSize, waitStrategyProperties, stopTimeout, TimeUnit.MILLISECONDS);
    }


    public PipelineExecutorImpl(String name, int bufferSize, WaitStrategyProperties waitStrategyProperties, long stopTimeout, TimeUnit stopTimeUnit) {
        super(name, stopTimeout, stopTimeUnit);

        EventFactory<PipelineEvent<T>> eventFactory = PipelineEvent::new;
        ThreadFactory threadFactory = new NamingThreadFactory("Pipeline-" + name);
        WaitStrategy waitStrategy = WaitMode.buildWaitStrategy(waitStrategyProperties);

        this.disruptor = new Disruptor<>(eventFactory, bufferSize, threadFactory, ProducerType.MULTI, waitStrategy);
    }

    @Override
    protected void start0() {
        if (stageDefinitionChain == null || stageDefinitionChain.isEmpty()) {
            throw PipelineException.of(String.format("At least one stage is required: %s", name));
        }

        EventHandlerGroup<PipelineEvent<T>> handlerGroup = disruptor.handleEventsWithWorkerPool(HEAD_HANDLER);
        while (!stageDefinitionChain.isEmpty()) {
            StageDefinition<T> stageDefinition = stageDefinitionChain.poll();
            int parallel = stageDefinition.parallel();

            StageHandler<T>[] workHandlers = new StageHandler[parallel];
            for (int i = 0; i < parallel; i++) {
                if (parallel > 1) {
                    workHandlers[i] = new StageHandler<>(stageDefinition.fork());
                } else {
                    workHandlers[i] = new StageHandler<>(stageDefinition);
                }

            }

            handlerGroup = handlerGroup.thenHandleEventsWithWorkerPool(workHandlers);
        }
        handlerGroup.thenHandleEventsWithWorkerPool(TAIL_HANDLER);

        disruptor.start();
    }

    @Override
    protected void stop0(long timeout, TimeUnit timeUnit) {
        if (timeout > 0) {
            try {
                disruptor.shutdown(timeout, timeUnit);
            } catch (TimeoutException e) {
                Logs.error(log, "Pipeline stop timeout:{}", name);
            }
        }
        disruptor.shutdown();
    }

    @Override
    public boolean submit0(T data) {
        disruptor.publishEvent(EVENT_TRANSLATOR, data);
        return true;
    }

    @Override
    public boolean trySubmit0(T data) {
        RingBuffer<PipelineEvent<T>> ringBuffer = disruptor.getRingBuffer();
        return ringBuffer.tryPublishEvent(EVENT_TRANSLATOR, data);
    }
}
