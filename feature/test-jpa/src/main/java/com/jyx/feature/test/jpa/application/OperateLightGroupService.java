package com.jyx.feature.test.jpa.application;

import com.jyx.feature.test.jpa.application.dto.LightGroupDto;
import com.jyx.feature.test.jpa.domain.entity.LightGroup;

import java.util.List;

/**
 * @author asa
 * @since 2021/11/6 20:44
 */
public interface OperateLightGroupService {

    /**
     * 新增灯组
     * @param lightGroupDtoList
     * @return 新增的灯组
     */
    List<LightGroup> create(List<LightGroupDto> lightGroupDtoList);

    LightGroup modify(LightGroupDto lightGroupDto);

    /**
     * 根据通道编号查询灯组
     * @param channelNumberList
     * @return
     */
    List<LightGroup> queryByChannelNumberList(List<Integer> channelNumberList);
}
