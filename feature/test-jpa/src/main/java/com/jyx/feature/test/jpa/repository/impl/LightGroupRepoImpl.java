package com.jyx.feature.test.jpa.repository.impl;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.feature.test.jpa.domain.repository.LightGroupRepo;
import com.jyx.feature.test.jpa.repository.LightGroupJpaRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2021/11/6 20:17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LightGroupRepoImpl implements LightGroupRepo {

    private final LightGroupJpaRepo lightGroupJpaRepo;

    @Override
    public List<LightGroup> save(List<LightGroup> lightGroupList) {
        return lightGroupJpaRepo.saveAll(lightGroupList);
    }

    @Override
    public List<LightGroup> queryByChannelNumberList(List<Integer> channelNumberList) {
        return lightGroupJpaRepo.findByChannelListNumberIn(channelNumberList);
    }
}
