package com.jyx.feature.test.jpa.domain.repository;

import com.jyx.feature.test.jpa.domain.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JYX
 * @since 2021/10/20 20:00
 */
@Repository
public interface ChannelJpaRepo  extends JpaRepository<Channel,Long> {

}
