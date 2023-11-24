package com.jyx.feature.test.mybatis.controller;

import com.jyx.feature.test.mybatis.domain.entity.LightGroup;
import com.jyx.feature.test.mybatis.repository.repo1.LightGroup1Mapper;
import com.jyx.feature.test.mybatis.repository.repo2.LightGroup2Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiangyaxin
 * @since 2021/11/7 9:21
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lightGroup")
public class LightGroupResource {

    private final LightGroup1Mapper lightGroup1Mapper;

    private final LightGroup2Mapper lightGroup2Mapper;

    @GetMapping
    public List<LightGroup> queryByChannelNumberList() {
        return Arrays.asList(
                lightGroup1Mapper.selectById(1L),
                lightGroup2Mapper.selectById(2L)
        );
    }
}
