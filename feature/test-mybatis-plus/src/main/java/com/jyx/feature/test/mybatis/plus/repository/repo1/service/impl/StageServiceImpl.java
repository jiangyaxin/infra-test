package com.jyx.feature.test.mybatis.plus.repository.repo1.service.impl;

import com.jyx.feature.test.mybatis.plus.domain.entity.Stage;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.StageMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.StageService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author CodeGenerator
 * @since 2023-10-30 14:15:09
 */
@Service
public class StageServiceImpl extends DbServiceImpl<StageMapper, Stage> implements StageService {

}
