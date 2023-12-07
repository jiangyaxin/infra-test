package com.jyx.feature.test.mybatis.plus.config;

import com.jyx.feature.test.mybatis.plus.repository.repo1.service.ShardingTableRuleService;
import com.jyx.infra.sharding.sphere.rules.ShardingTableRule;
import com.jyx.infra.sharding.sphere.rules.ShardingTableRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Archforce
 * @since 2023/12/7 15:23
 */
@ConditionalOnClass(ShardingTableRuleRepository.class)
@Component
@RequiredArgsConstructor
public class MybatisPlusShardingTableRuleRepository implements ShardingTableRuleRepository {

    private final ShardingTableRuleService shardingTableRuleService;

    @Override
    public List<? extends ShardingTableRule> load() {
        return shardingTableRuleService.list();
    }
}
