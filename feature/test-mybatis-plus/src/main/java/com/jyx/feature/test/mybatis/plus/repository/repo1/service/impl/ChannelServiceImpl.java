package com.jyx.feature.test.mybatis.plus.repository.repo1.service.impl;

import com.jyx.feature.test.mybatis.plus.domain.entity.Channel;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.ChannelMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.ChannelService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author jiangyaxin
 * @since 2023/10/26 14:51
 */
@Service
public class ChannelServiceImpl extends DbServiceImpl<ChannelMapper, Channel> implements ChannelService {

}
