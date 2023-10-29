package com.jyx.feature.test.mybatis.plus.repository.repo2.service.impl;

import com.jyx.feature.test.mybatis.plus.domain.entity.LightGroup;
import com.jyx.feature.test.mybatis.plus.repository.repo2.mapper.LightGroupMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo2.service.LightGroupService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Archforce
 * @since 2023/10/26 14:51
 */
@Service
public class LightGroupServiceImpl extends DbServiceImpl<LightGroupMapper, LightGroup> implements LightGroupService {

}
