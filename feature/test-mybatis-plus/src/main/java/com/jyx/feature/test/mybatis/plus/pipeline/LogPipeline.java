package com.jyx.feature.test.mybatis.plus.pipeline;

import com.jyx.infra.spring.pipeline.Pipeline;
import com.jyx.infra.spring.pipeline.Stage;

/**
 * @author Archforce
 * @since 2023/11/3 14:27
 */
@Pipeline
public class LogPipeline {

    @Stage
    public void stage1(Integer param) {

    }
}
