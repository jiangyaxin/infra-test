package com.jyx.infra.sharding.sphere.rules;

import com.jyx.infra.constant.StringConstant;
import com.jyx.infra.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.audit.ShardingAuditStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.ShardingStrategyConfiguration;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Archforce
 * @since 2023/12/6 17:44
 */
@Slf4j
public class ShardingTableRuleTransformer {

    public static ShardingTableRuleConfiguration toRuleConfiguration(ShardingTableRule tableRule) {
        ShardingTableRuleConfiguration configuration = new ShardingTableRuleConfiguration(tableRule.getLogicTable(), tableRule.getActualDataNodes());

        ShardingStrategyConfiguration databaseStrategy = ShardingStrategyType.buildStrategyConfiguration(tableRule.getDatabaseShardingStrategyType(), tableRule.getDatabaseShardingStrategyParam());
        ShardingStrategyConfiguration tableStrategy = ShardingStrategyType.buildStrategyConfiguration(tableRule.getTableShardingStrategyType(), tableRule.getTableShardingStrategyParam());
        KeyGenerateStrategyConfiguration keyGenerateStrategy = buildKeyGenerateStrategy(tableRule.getKeyGenerateStrategyParam());
        ShardingAuditStrategyConfiguration auditStrategy = buildAuditStrategy(tableRule.getAuditStrategyParam());

        configuration.setDatabaseShardingStrategy(databaseStrategy);
        configuration.setTableShardingStrategy(tableStrategy);
        configuration.setKeyGenerateStrategy(keyGenerateStrategy);
        configuration.setTableShardingStrategy(tableStrategy);
        configuration.setAuditStrategy(auditStrategy);
        return configuration;
    }

    private static KeyGenerateStrategyConfiguration buildKeyGenerateStrategy(String param) {
        if (param == null || ObjectUtils.isEmpty(param.trim())) {
            return null;
        }
        Map<String, Object> paramMap = JsonUtil.toMap(param.trim());
        String column = (String) Optional.ofNullable(paramMap.get(ShardingRuleStrategyConstants.COLUMN)).orElse(StringConstant.EMPTY);
        String keyGeneratorName = (String) Optional.ofNullable(paramMap.get(ShardingRuleStrategyConstants.KEY_GENERATOR_NAME)).orElse(StringConstant.EMPTY);

        return new KeyGenerateStrategyConfiguration(column, keyGeneratorName);
    }

    private static ShardingAuditStrategyConfiguration buildAuditStrategy(String param) {
        if (param == null || ObjectUtils.isEmpty(param.trim())) {
            return null;
        }
        Map<String, Object> paramMap = JsonUtil.toMap(param.trim());

        Collection<String> auditorNames = (Collection<String>) Optional.ofNullable(paramMap.get(ShardingRuleStrategyConstants.AUDITOR_NAMES)).orElse(new ArrayList<>(0));
        Boolean allowHintDisable = (Boolean) Optional.ofNullable(paramMap.get(ShardingRuleStrategyConstants.ALLOW_HINT_DISABLE)).orElse(false);

        return new ShardingAuditStrategyConfiguration(auditorNames, allowHintDisable);
    }
}
