package com.jyx.feature.test.jpa;

import com.jyx.feature.test.jpa.domain.entity.Channel;
import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.feature.test.jpa.domain.repository.ChannelJpaRepo;
import com.jyx.feature.test.jpa.domain.repository.LightGroupJpaRepo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

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
        Channel channel1 = Channel.builder().number(3).build();
        Channel channel2 = Channel.builder().number(4).build();
        LightGroup lightGroup = LightGroup.builder()
                .number(2)
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

    @Test
    void testQueryLightGroup() {
        List<LightGroup> byChannelListNumber = lightGroupJpaRepo.findByChannelListNumber(3);
        Assert.isTrue(byChannelListNumber.size() > 0 , "byChannelListNumber > 0");
        byChannelListNumber.forEach( lightGroup -> {
            List<Channel> channelList = lightGroup.getChannelList();
            Assert.isTrue(channelList.size() > 0 , "channelList > 0");
        });
    }
}
