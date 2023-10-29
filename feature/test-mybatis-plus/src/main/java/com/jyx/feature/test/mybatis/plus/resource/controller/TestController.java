package com.jyx.feature.test.mybatis.plus.resource.controller;

import com.jyx.feature.test.mybatis.plus.domain.entity.Channel;
import com.jyx.feature.test.mybatis.plus.domain.entity.LightGroup;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.ChannelMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.ChannelService;
import com.jyx.feature.test.mybatis.plus.repository.repo2.mapper.LightGroupMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo2.service.LightGroupService;
import com.jyx.infra.mybatis.plus.DbHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public void dataSourceTest() {
        Channel channel = channelMapper.selectById(2L);
        LightGroup lightGroup = lightGroupMapper.selectById(1L);
        Channel channelServiceById = channelService.getById(2L);
        LightGroup lightGroupServiceById = lightGroupService.getById(1L);
        log.error("1");
    }
}
