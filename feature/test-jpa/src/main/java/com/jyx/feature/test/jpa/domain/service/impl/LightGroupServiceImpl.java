package com.jyx.feature.test.jpa.domain.service.impl;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.feature.test.jpa.domain.repository.LightGroupRepo;
import com.jyx.feature.test.jpa.domain.service.LightGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author asa
 * @since 2021/11/6 20:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LightGroupServiceImpl implements LightGroupService {

    private final LightGroupRepo lightGroupRepo;

    @Override
    public List<LightGroup> save(List<LightGroup> lightGroupList) {
        return lightGroupRepo.save(lightGroupList);
    }

    @Override
    public List<LightGroup> queryByChannelNumberList(List<Integer> channelNumberList) {
        return lightGroupRepo.queryByChannelNumberList(channelNumberList);
    }
}
