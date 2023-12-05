package com.jyx.feature.test.mybatis.plus.repository.repo1.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.jyx.feature.test.mybatis.plus.domain.entity.Stage;
import com.jyx.infra.mybatis.plus.service.DbService;

import java.util.List;

/**
 * @author CodeGenerator
 * @since 2023-12-05 15:17:09
 */
public interface StageService extends DbService<Stage> {

    List<Stage> shardingList(Wrapper<Stage> wrapper);

}
