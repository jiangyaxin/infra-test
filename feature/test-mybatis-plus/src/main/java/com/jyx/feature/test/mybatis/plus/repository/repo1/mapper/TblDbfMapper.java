package com.jyx.feature.test.mybatis.plus.repository.repo1.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jyx.feature.test.mybatis.plus.domain.entity.TblDbf;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CodeGenerator
 * @since 2023-11-07 18:18:34
 */
@DS("test1")
@Mapper
public interface TblDbfMapper extends BizBaseMapper<TblDbf> {

}
