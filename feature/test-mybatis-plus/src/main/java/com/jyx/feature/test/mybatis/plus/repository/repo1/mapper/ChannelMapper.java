package com.jyx.feature.test.mybatis.plus.repository.repo1.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jyx.feature.test.mybatis.plus.domain.entity.Channel;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Archforce
 * @since 2023/10/25 18:20
 */
@Mapper
@DS("test1")
public interface ChannelMapper extends BizBaseMapper<Channel> {

    Channel selectById(Long id);
}
