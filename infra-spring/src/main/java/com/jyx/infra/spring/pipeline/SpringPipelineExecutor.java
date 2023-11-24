package com.jyx.infra.spring.pipeline;

import com.jyx.infra.pipeline.PipelineException;
import com.jyx.infra.pipeline.StageDefinition;
import com.jyx.infra.pipeline.disruptor.PipelineExecutorImpl;
import com.jyx.infra.pipeline.disruptor.WaitStrategyProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangyaxin
 * @since 2023/11/4 14:57
 */
public class SpringPipelineExecutor extends PipelineExecutorImpl<Object> implements InitializingBean, DisposableBean {

    private final Class<?> dataClass;

    public SpringPipelineExecutor(String name, int bufferSize,
                                  WaitStrategyProperties waitStrategyProperties,
                                  long stopTimeout, TimeUnit stopTimeUnit,
                                  Class<?> dataClass, Iterable<StageDefinition<Object>> stageIterable) {
        super(name, bufferSize, waitStrategyProperties, stopTimeout, stopTimeUnit);
        this.dataClass = dataClass;
        addStage(stageIterable);
    }

    @Override
    protected void preSubmit(Object data) {
        if (data.getClass() != dataClass) {
            throw new PipelineException(String.format("Pipeline only can submit %s type data: %s", dataClass.getName(), name));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }
}
