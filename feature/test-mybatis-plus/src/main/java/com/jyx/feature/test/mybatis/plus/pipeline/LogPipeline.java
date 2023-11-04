package com.jyx.feature.test.mybatis.plus.pipeline;

import com.jyx.feature.test.mybatis.plus.repository.repo1.service.StageService;
import com.jyx.infra.spring.pipeline.Pipeline;
import com.jyx.infra.spring.pipeline.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Archforce
 * @since 2023/11/3 14:27
 */
@Slf4j
@Pipeline
@RequiredArgsConstructor
public class LogPipeline {

    private final StageService stageService;

    @Stage(order = 1)
    public void stage1(Integer param) {
        log.info("阶段1 消费 {}", stageService.getById(param));
    }

    @Stage(order = 2)
    public void stage2(Integer param) {
        log.info("阶段2 消费 {}", param);
    }
}
