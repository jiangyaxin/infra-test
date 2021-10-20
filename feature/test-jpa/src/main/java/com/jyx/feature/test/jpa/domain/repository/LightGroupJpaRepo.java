package com.jyx.feature.test.jpa.domain.repository;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JYX
 * @since 2021/10/20 17:50
 */
@Repository
public interface LightGroupJpaRepo extends JpaRepository<LightGroup,Long> {

    @EntityGraph("fetchChannelList")
    List<LightGroup> findByChannelListNumber(Integer channelNumber);
}
