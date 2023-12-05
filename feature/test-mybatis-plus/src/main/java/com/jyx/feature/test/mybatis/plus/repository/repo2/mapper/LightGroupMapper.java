package com.jyx.feature.test.mybatis.plus.repository.repo2.mapper;

import com.jyx.feature.test.mybatis.plus.domain.entity.LightGroup;
import com.jyx.infra.mybatis.plus.mapper.BizBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jiangyaxin
 * @since 2023/10/25 18:20
 */
@Mapper
public interface LightGroupMapper extends BizBaseMapper<LightGroup> {

    LightGroup selectById(Long id);
}
