package com.jyx.infra.pipeline.disruptor;

import com.jyx.infra.log.Logs;
import com.jyx.infra.pipeline.AbstractPipeline;
import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.PipelineException;
import com.jyx.infra.pipeline.Stage;
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
public class PipelineImpl<T> extends AbstractPipeline<T> {

    private final Disruptor<PipelineEvent<T>> disruptor;

    private final WorkHandler<PipelineEvent<T>> HEAD_HANDLER = event -> Logs.debug(log, "Start process sequence:{} data:{}", event.sequence(), event.data());

    private final WorkHandler<PipelineEvent<T>> TAIL_HANDLER = event -> {
        Logs.debug(log, "Finish process sequence:{} data:{}", event.sequence(), event.data());
        event.clear();
    };

    private final EventTranslatorOneArg<PipelineEvent<T>, T> EVENT_TRANSLATOR = (event, sequence, arg0) -> event.load(sequence, arg0);

    public PipelineImpl(String name, int bufferSize, WaitStrategyProperties waitStrategyProperties) {
        super(name);

        EventFactory<PipelineEvent<T>> eventFactory = PipelineEvent::new;
        ThreadFactory threadFactory = new NamingThreadFactory("Pipeline-" + name);
        WaitStrategy waitStrategy = WaitMode.buildWaitStrategy(waitStrategyProperties);

        this.disruptor = new Disruptor<>(eventFactory, bufferSize, threadFactory, ProducerType.MULTI, waitStrategy);
    }

    @Override
    protected void start0() {
        if (stageChain == null || stageChain.isEmpty()) {
            throw PipelineException.of(String.format("At least one stage is required: %s", name));
        }

        EventHandlerGroup<PipelineEvent<T>> handlerGroup = disruptor.handleEventsWithWorkerPool(HEAD_HANDLER);
        while (!stageChain.isEmpty()) {
            Stage<T> stage = stageChain.poll();
            int parallel = stage.parallel();

            StageHandler<T>[] workHandlers = new StageHandler[parallel];
            for (int i = 0; i < parallel; i++) {
                if (parallel > 1) {
                    workHandlers[i] = new StageHandler<>(stage.fork());
                } else {
                    workHandlers[i] = new StageHandler<>(stage);
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
