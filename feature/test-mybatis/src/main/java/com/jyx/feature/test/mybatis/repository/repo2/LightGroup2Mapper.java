package com.jyx.feature.test.mybatis.repository.repo2;

import com.jyx.feature.test.mybatis.domain.entity.LightGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jiangyaxin
 * @since 2023/10/19 12:31
 */
@Mapper
public interface LightGroup2Mapper {

    LightGroup selectById(Long id);
}
