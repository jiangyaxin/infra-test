package com.jyx.feature.test.mybatis.controller;

import com.jyx.feature.test.mybatis.domain.entity.LightGroup;
import com.jyx.feature.test.mybatis.repository.repo1.LightGroupMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author asa
 * @since 2021/11/7 9:21
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lightGroup")
public class LightGroupResource {

    private final LightGroupMapper lightGroupMapper;

    @GetMapping
    public LightGroup queryByChannelNumberList() {
        return lightGroupMapper.selectById(1L);
    }
}
