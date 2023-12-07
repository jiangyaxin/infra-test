package com.jyx.feature.test.mybatis.plus.repository.repo1.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jyx.feature.test.mybatis.plus.domain.entity.MybatisPlusShardingTableRule;
import com.jyx.feature.test.mybatis.plus.repository.repo1.mapper.ShardingTableRuleMapper;
import com.jyx.feature.test.mybatis.plus.repository.repo1.service.ShardingTableRuleService;
import com.jyx.infra.mybatis.plus.service.DbServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author CodeGenerator
 * @since 2023-12-07 15:37:59
 */
@DS("acp_base")
@Service
public class ShardingTableRuleServiceImpl extends DbServiceImpl<ShardingTableRuleMapper, MybatisPlusShardingTableRule> implements ShardingTableRuleService {

}
