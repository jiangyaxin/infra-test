package com.jyx.feature.test.jdk;

import com.jyx.infra.pipeline.PipelineExecutor;
import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.StageDefinition;
import com.jyx.infra.pipeline.disruptor.PipelineExecutorImpl;
import com.jyx.infra.pipeline.disruptor.StageDefinitionImpl;
import com.jyx.infra.pipeline.disruptor.WaitMode;
import com.jyx.infra.pipeline.disruptor.WaitStrategyProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author jiangyaxin
 * @since 2023/11/1 19:42
 */
@Slf4j
public class PipelineTest {

    @Test
    public void test() throws InterruptedException {
        WaitStrategyProperties properties = new WaitStrategyProperties(WaitMode.BLOCKING);
        PipelineExecutor<Integer> pipelineExecutor = new PipelineExecutorImpl<>("test", 1024, properties);
        List<StageDefinition<Integer>> stageDefinitionList = new ArrayList<>();

        Consumer<PipelineEvent<Integer>> consumer = event -> log.info("{} 被 {} 消费了，sequence = {}", event.data(), event.stage().name(), event.sequence());

        stageDefinitionList.add(new StageDefinitionImpl("阶段1", 1, consumer));
        stageDefinitionList.add(new StageDefinitionImpl("阶段2", 2, consumer));
//        stageList.add(new StageImpl("阶段3", 1, consumer));
//        stageList.add(new StageImpl("阶段4", 1, consumer));

        pipelineExecutor.addStage(stageDefinitionList);
        pipelineExecutor.start();

        int i = 100;
        while (i-- > 0) {
            pipelineExecutor.submit(i);
        }

        Thread.currentThread().join();
    }
}
