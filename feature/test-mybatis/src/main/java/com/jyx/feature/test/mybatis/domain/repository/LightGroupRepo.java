package com.jyx.feature.test.mybatis.domain.repository;

import com.jyx.feature.test.mybatis.domain.entity.LightGroup;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2021/11/6 20:16
 */
public interface LightGroupRepo {
    /**
     * 批量保存灯组
     */
    List<LightGroup> save(List<LightGroup> lightGroupList);

    List<LightGroup> queryByChannelNumberList(List<Integer> channelNumberList);
}
