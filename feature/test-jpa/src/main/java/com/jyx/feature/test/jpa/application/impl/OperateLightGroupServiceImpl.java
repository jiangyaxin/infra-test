package com.jyx.feature.test.jpa.application.impl;

import com.google.common.collect.Lists;
import com.jyx.feature.test.jpa.application.OperateLightGroupService;
import com.jyx.feature.test.jpa.application.dto.LightGroupDto;
import com.jyx.feature.test.jpa.application.dto.assembler.LightGroupAssembler;
import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.feature.test.jpa.domain.service.LightGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangyaxin
 * @since 2021/11/6 20:45
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLightGroupServiceImpl implements OperateLightGroupService {

    private final LightGroupService lightGroupService;

    @Override
    public List<LightGroup> create(List<LightGroupDto> lightGroupDtoList) {
        return lightGroupService.save(
                lightGroupDtoList.stream()
                        .map(LightGroupAssembler::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public LightGroup modify(LightGroupDto lightGroupDto) {
        return lightGroupService.save(Lists.newArrayList(LightGroupAssembler.toEntity(lightGroupDto))).get(0);
    }

    @Override
    public List<LightGroup> queryByChannelNumberList(List<Integer> channelNumberList) {
        return lightGroupService.queryByChannelNumberList(channelNumberList);
    }
}
