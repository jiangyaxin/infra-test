package com.jyx.infra.sharding.sphere.rules;

import java.util.List;

/**
 * @author Archforce
 * @since 2023/12/6 16:40
 */
public interface ShardingTableRuleRepository {

    List<? extends ShardingTableRule> load();
}
