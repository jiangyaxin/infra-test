package com.jyx.feature.test.mybatis.repository.repo1;

import com.jyx.feature.test.mybatis.domain.entity.LightGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Archforce
 * @since 2023/10/19 12:31
 */
@Mapper
public interface LightGroup1Mapper {

    LightGroup selectById(Long id);
}
