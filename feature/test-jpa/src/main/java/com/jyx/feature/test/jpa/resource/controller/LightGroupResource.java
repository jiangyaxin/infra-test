package com.jyx.feature.test.jpa.resource.controller;

import com.jyx.feature.test.jpa.application.OperateLightGroupService;
import com.jyx.feature.test.jpa.application.dto.LightGroupDto;
import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.infra.exception.BusinessException;
import com.jyx.infra.web.validation.ValidList;
import com.jyx.infra.web.validation.groups.Create;
import com.jyx.infra.web.validation.groups.Modify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    private final OperateLightGroupService operateLightGroupService;

    @PostMapping
    public List<LightGroup> create(@RequestBody @Validated({Create.class}) @NotEmpty(message = "灯组不能为空") ValidList<LightGroupDto> lightGroupDtoList){
        return operateLightGroupService.create(lightGroupDtoList);
    }

    @PutMapping
    public LightGroup modify(@RequestBody @Validated({Modify.class}) LightGroupDto lightGroupDto){
        return operateLightGroupService.modify(lightGroupDto);
    }

    @GetMapping("/channelNumber/{channelNumberList}")
    public List<LightGroup> queryByChannelNumberList(@PathVariable List<Integer> channelNumberList){
        return operateLightGroupService.queryByChannelNumberList(channelNumberList);
    }
}
