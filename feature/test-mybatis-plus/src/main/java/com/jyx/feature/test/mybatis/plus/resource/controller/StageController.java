package com.jyx.feature.test.mybatis.plus.resource.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jyx.feature.test.mybatis.plus.domain.entity.Stage;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.StageService;
import com.jyx.feature.test.mybatis.plus.resource.request.StageRequest;
import com.jyx.infra.mybatis.plus.query.QueryBuilder;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/10/25 15:34
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/test/stage")
public class StageController {

    private final StageService stageService;


    @ApiOperation(value = "阶段测试接口")
    @GetMapping
    public List<Stage> stageTest(StageRequest stageRequest) {
        QueryWrapper<Stage> query = QueryBuilder.build(stageRequest, Stage.class);
        List<Stage> stageList = stageService.list(query);
        return stageList;
    }

    @ApiOperation(value = "阶段测试接口")
    @GetMapping("/sharding")
    public List<Stage> shardingStageTest(StageRequest stageRequest) {
        QueryWrapper<Stage> query = QueryBuilder.build(stageRequest, Stage.class);
        List<Stage> stageList = stageService.shardingList(query);
        return stageList;
    }
}
