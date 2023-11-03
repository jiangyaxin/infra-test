package com.jyx.infra.spring.pipeline;

import com.jyx.infra.pipeline.PipelineExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Archforce
 * @since 2023/11/3 21:08
 */
@Component
public class PipelineHolder {

    private final Map<Class<?>, PipelineExecutor<?>> pipelineMap = new HashMap<>();

    public void registerPipeline(Class<?> clazz, PipelineExecutor<?> pipelineExecutor) {
        pipelineMap.put(clazz, pipelineExecutor);
    }

    @PreDestroy
    public void destroy() {
        pipelineMap.values().forEach(pipelineExecutor -> pipelineExecutor.stop());
    }

}
