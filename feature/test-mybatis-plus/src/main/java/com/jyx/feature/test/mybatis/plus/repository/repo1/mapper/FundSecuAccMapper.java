package com.jyx.feature.test.mybatis.plus.repository.repo1.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jyx.feature.test.mybatis.plus.domain.entity.FundSecuAcc;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CodeGenerator
 * @since 2023-11-23 13:24:31
 */
@DS("acp_base")
@Mapper
public interface FundSecuAccMapper extends BizBaseMapper<FundSecuAcc> {

}
