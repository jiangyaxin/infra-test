package com.jyx.feature.test.mybatis.plus.repository.repo1.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jyx.feature.test.mybatis.plus.domain.entity.Stage;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CodeGenerator
 * @since 2023-10-30 11:25:51
 */
@DS("test1")
@Mapper
public interface StageMapper extends BizBaseMapper<Stage> {

}
