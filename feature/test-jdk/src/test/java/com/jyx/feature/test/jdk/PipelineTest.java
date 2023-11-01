package com.jyx.feature.test.jdk;

import com.jyx.infra.pipeline.Pipeline;
import com.jyx.infra.pipeline.PipelineEvent;
import com.jyx.infra.pipeline.Stage;
import com.jyx.infra.pipeline.disruptor.PipelineImpl;
import com.jyx.infra.pipeline.disruptor.StageImpl;
import com.jyx.infra.pipeline.disruptor.WaitMode;
import com.jyx.infra.pipeline.disruptor.WaitStrategyProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Archforce
 * @since 2023/11/1 19:42
 */
@Slf4j
public class PipelineTest {

    @Test
    public void test() throws InterruptedException {
        WaitStrategyProperties properties = new WaitStrategyProperties();
        properties.setWaitMode(WaitMode.BLOCKING);
        Pipeline<Integer> pipeline = new PipelineImpl<>("test", 1024, properties);
        List<Stage<Integer>> stageList = new ArrayList<>();

        Consumer<PipelineEvent<Integer>> consumer = event -> log.info("{} 被 {} 消费了，sequence = {}", event.data(), event.stage().name(), event.sequence());

        stageList.add(new StageImpl("阶段1", 1, consumer));
        stageList.add(new StageImpl("阶段2", 2, consumer));
//        stageList.add(new StageImpl("阶段3", 1, consumer));
//        stageList.add(new StageImpl("阶段4", 1, consumer));

        pipeline.addStage(stageList);
        pipeline.start();

        int i = 100;
        while (i-- > 0) {
            pipeline.submit(i);
        }

        Thread.currentThread().join();
    }
}
