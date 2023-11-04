package com.jyx.feature.test.mybatis.plus.resource.controller;

import com.jyx.feature.test.mybatis.plus.domain.entity.Channel;
import com.jyx.feature.test.mybatis.plus.domain.entity.LightGroup;
import com.jyx.feature.test.mybatis.plus.domain.entity.Stage;
import com.jyx.feature.test.mybatis.plus.pipeline.LogPipeline;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.ChannelMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.ChannelService;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.StageService;
import com.jyx.feature.test.mybatis.plus.repository.repo2.mapper.LightGroupMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo2.service.LightGroupService;
import com.jyx.infra.mybatis.plus.DbHolder;
import com.jyx.infra.mybatis.plus.metadata.ColumnInfo;
import com.jyx.infra.spring.pipeline.PipelineHolder;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Archforce
 * @since 2023/10/25 15:34
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/test")
public class TestController {

    private final DbHolder dbHolder;

    private final ChannelMapper channelMapper;

    private final LightGroupMapper lightGroupMapper;

    private final ChannelService channelService;

    private final LightGroupService lightGroupService;

    private final StageService stageService;

    private final PipelineHolder pipelineHolder;


    @ApiOperation(value = "测试接口")
    @GetMapping
    public void dataSourceTest() {
        Channel channel = channelMapper.selectById(2L);
        LightGroup lightGroup = lightGroupMapper.selectById(1L);
        Channel channelServiceById = channelService.getById(2L);
        LightGroup lightGroupServiceById = lightGroupService.getById(1L);
        Stage stageServiceById= stageService.getById(3L);

        List<ColumnInfo> columnInfoList = dbHolder.columnInfo(Stage.class);
        log.error("1");
    }

    @ApiOperation(value = "Pipeline测试接口")
    @GetMapping("/pipeline/{logId}")
    public void pipelineTest(@PathVariable Integer logId) {
        pipelineHolder.getPipeline(LogPipeline.class).submit(logId);
    }
}
