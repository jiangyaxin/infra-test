package com.jyx.feature.test.jpa;

import com.jyx.feature.test.jpa.domain.entity.Channel;
import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.feature.test.jpa.domain.repository.ChannelJpaRepo;
import com.jyx.feature.test.jpa.domain.repository.LightGroupJpaRepo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JYX
 * @since 2021/10/20 18:03
 */
@SpringBootTest
public class LightGroupTest {

    @Autowired
    LightGroupJpaRepo lightGroupJpaRepo;

    @Autowired
    ChannelJpaRepo channelJpaRepo;

    @Test
    void testSaveLightGroup() {
        Channel channel1 = Channel.builder().number(1).build();
        Channel channel2 = Channel.builder().number(2).build();
        LightGroup lightGroup = LightGroup.builder()
                .number(1)
                .type(1)
                .direction(1)
                .flowDirection(1)
                .channelList(Lists.newArrayList(
                    channel1,channel2
                ))
                .build();
        channelJpaRepo.save(channel1);
        channelJpaRepo.save(channel2);
        lightGroupJpaRepo.save(lightGroup);
    }
}
