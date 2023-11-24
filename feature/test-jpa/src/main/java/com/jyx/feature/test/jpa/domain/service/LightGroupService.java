package com.jyx.feature.test.jpa.domain.service;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2021/11/6 20:38
 */
public interface LightGroupService {

    List<LightGroup> save(List<LightGroup> lightGroupList);

    List<LightGroup> queryByChannelNumberList(List<Integer> channelNumberList);
}
