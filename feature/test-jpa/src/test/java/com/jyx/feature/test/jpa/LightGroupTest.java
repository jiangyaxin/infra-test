package com.jyx.feature.test.jpa;

import com.google.common.collect.Lists;
import com.jyx.feature.test.jpa.application.OperateLightGroupService;
import com.jyx.feature.test.jpa.application.dto.ChannelDto;
import com.jyx.feature.test.jpa.application.dto.LightGroupDto;
import com.jyx.feature.test.jpa.domain.entity.Channel;
import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import com.jyx.feature.test.jpa.domain.repository.LightGroupRepo;
import com.jyx.feature.test.jpa.domain.service.LightGroupService;
import com.jyx.feature.test.jpa.repository.ChannelJpaRepo;
import com.jyx.feature.test.jpa.repository.LightGroupJpaRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;

import static com.jyx.feature.test.jpa.domain.entity.value.Direction.WEST_NORTH;
import static com.jyx.feature.test.jpa.domain.entity.value.Direction.WEST_SOUTH;
import static com.jyx.feature.test.jpa.domain.entity.value.FlowDirection.LEFT_STRAIGHT_RIGHT;
import static com.jyx.feature.test.jpa.domain.entity.value.FlowDirection.TURN_ROUND_LEFT_STRAIGHT_RIGHT;
import static com.jyx.feature.test.jpa.domain.entity.value.LightGroupType.VEHICLE;

/**
 * @author jiangyaxin
 * @since 2021/10/20 18:03
 */
@SpringBootTest
@Slf4j
public class LightGroupTest {

    @Autowired
    LightGroupJpaRepo lightGroupJpaRepo;

    @Autowired
    ChannelJpaRepo channelJpaRepo;

    @Autowired
    LightGroupRepo lightGroupRepo;

    @Autowired
    LightGroupService lightGroupService;

    @Autowired
    OperateLightGroupService operateLightGroupService;

    @Test
    void testSaveLightGroupListByApplicationService() {
        ChannelDto channel1 = ChannelDto.builder().number(3).build();
        ChannelDto channel2 = ChannelDto.builder().number(4).build();
        LightGroupDto lightGroup = LightGroupDto.builder()
                .number(2)
                .type(VEHICLE)
                .direction(WEST_SOUTH)
                .flowDirection(TURN_ROUND_LEFT_STRAIGHT_RIGHT)
                .channelList(Lists.newArrayList(
                        channel1,channel2
                ))
                .build();
        operateLightGroupService.create(Lists.newArrayList(lightGroup));
    }

    @Test
    void testSaveLightGroupListByService() {
        Channel channel1 = Channel.builder().number(3).build();
        Channel channel2 = Channel.builder().number(4).build();
        LightGroup lightGroup = LightGroup.builder()
                .number(2)
                .type(VEHICLE)
                .direction(WEST_NORTH)
                .flowDirection(LEFT_STRAIGHT_RIGHT)
                .channelList(Lists.newArrayList(
                        channel1,channel2
                ))
                .build();
        lightGroupService.save(Lists.newArrayList(lightGroup));
    }

    @Test
    void testSaveLightGroupListByRepo() {
        Channel channel1 = Channel.builder().number(3).build();
        Channel channel2 = Channel.builder().number(4).build();
        LightGroup lightGroup = LightGroup.builder()
                .number(2)
                .type(VEHICLE)
                .direction(WEST_NORTH)
                .flowDirection(LEFT_STRAIGHT_RIGHT)
                .channelList(Lists.newArrayList(
                        channel1,channel2
                ))
                .build();
        lightGroupRepo.save(Lists.newArrayList(lightGroup));
    }

    @Test
    void testSaveLightGroup() {
        Channel channel1 = Channel.builder().number(3).build();
        Channel channel2 = Channel.builder().number(4).build();
        LightGroup lightGroup = LightGroup.builder()
                .number(2)
                .type(VEHICLE)
                .direction(WEST_NORTH)
                .flowDirection(LEFT_STRAIGHT_RIGHT)
                .channelList(Lists.newArrayList(
                    channel1,channel2
                ))
                .build();
//        channelJpaRepo.save(channel1);
//        channelJpaRepo.save(channel2);
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

    @Test
    void testQueryChannel() {
        Channel channel = channelJpaRepo.findById(7341907357220866L).get();
        Assert.notNull(channel.getLightGroup(),"channel light group is null");
    }

    @Test
    void testQueryLightGroupByExample() {
        Channel channel1 = Channel.builder().number(3).build();
        Channel channel2 = Channel.builder().number(4).build();
        LightGroup lightGroupProbe = LightGroup.builder()
                .channelList(Lists.newArrayList(
                        channel1,channel2
                ))
                .build();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues();
        Example<LightGroup> lightGroupExample = Example.of(lightGroupProbe, exampleMatcher);
        List<LightGroup> lightGroupList = lightGroupJpaRepo.findAll(lightGroupExample);
        Assert.isTrue(lightGroupList.size() > 0 , "byChannelListNumber > 0");
        lightGroupList.forEach( lightGroup -> Assert.isTrue(lightGroup.getChannelList().size() == 2 , "channel size != 2"));
    }

    @Test
    void testQueryLightGroupBySpecificationExecutor(){
        Specification<LightGroup> lightGroupSpecification = (root , criteriaQuery , criteriaBuilder) -> {
            Join<LightGroup, Channel> joinChannel = root.join("channelList", JoinType.LEFT);
            return criteriaBuilder.or(
                    criteriaBuilder.equal(joinChannel.get("number"), 3),
                    criteriaBuilder.equal(joinChannel.get("number"), 4)
            );
        };
        List<LightGroup> lightGroupList = lightGroupJpaRepo.findAll(lightGroupSpecification);
        log.info("灯组{}",lightGroupList);
        Assert.isTrue(lightGroupList.size() > 0 , "byChannelListNumber > 0");
        lightGroupList.forEach( lightGroup -> Assert.isTrue(lightGroup.getChannelList().size() == 2 , "channel size != 2"));
    }

    @Test
    void testDeleteLightGroup() {
        int delete = lightGroupJpaRepo.deleteByChannelListNumber(3);
    }
}
