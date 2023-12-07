package com.jyx.infra.sharding.sphere.rules;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Archforce
 * @since 2023/12/7 15:59
 */
@ConditionalOnBean(ShardingTableRuleUpdater.class)
@Validated
@RestController
@RequestMapping("/v1/sharding_table_rule")
@RequiredArgsConstructor
public class ShardingTableRuleController {

    private final ShardingTableRuleUpdater shardingTableRuleUpdater;

    @GetMapping(value = "/refresh")
    public Collection<RuleConfiguration> refresh(Boolean force) {
        Collection<RuleConfiguration> ruleConfigCollection = shardingTableRuleUpdater.refresh(Optional.ofNullable(force).orElse(true));

        return ruleConfigCollection;
    }
}
