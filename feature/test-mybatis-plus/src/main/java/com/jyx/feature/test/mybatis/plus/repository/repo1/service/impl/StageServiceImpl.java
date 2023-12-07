package com.jyx.feature.test.mybatis.plus.repository.repo1.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.jyx.feature.test.mybatis.plus.domain.entity.Stage;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.StageMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.StageService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import com.jyx.infra.sharding.sphere.ShardingSphereConstants;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CodeGenerator
 * @since 2023-12-05 15:17:09
 */
@DS("test1")
@Service
public class StageServiceImpl extends DbServiceImpl<StageMapper, Stage> implements StageService {

    @DS(ShardingSphereConstants.SHARDING_SPHERE_DS_NAME)
    @Override
    public List<Stage> shardingList(Wrapper<Stage> wrapper) {
        return super.list(wrapper);
    }
}
