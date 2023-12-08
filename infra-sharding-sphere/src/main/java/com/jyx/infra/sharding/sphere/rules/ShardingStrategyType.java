package com.jyx.infra.sharding.sphere.rules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyx.infra.constant.StringConstant;
import com.jyx.infra.exception.EnumException;
import com.jyx.infra.util.JsonUtil;
import lombok.Getter;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.*;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jiangyaxin
 * @since 2023/12/6 17:49
 */
@Getter
public enum ShardingStrategyType {

    COMPLEX("COMPLEX", ComplexShardingStrategyConfiguration.class),
    HINT("HINT", HintShardingStrategyConfiguration.class),
    STANDARD("STANDARD", StandardShardingStrategyConfiguration.class),
    NONE("NONE", NoneShardingStrategyConfiguration.class);

    private final String type;

    private final Class<? extends ShardingStrategyConfiguration> clazz;

    ShardingStrategyType(String type, Class<? extends ShardingStrategyConfiguration> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    private static Map<String, ShardingStrategyType> typeToIdentityMap = Arrays.stream(ShardingStrategyType.values())
            .collect(Collectors.toMap(
                    ShardingStrategyType::getType,
                    Function.identity(),
                    (o1, o2) -> {
                        throw EnumException.of(String.format("ShardingStrategyType error, %s and %s,the types are the same", o1.name(), o2.name()));
                    }));

    public static ShardingStrategyType getByType(String type) {
        ShardingStrategyType strategyType = typeToIdentityMap.get(type);
        if (strategyType == null) {
            throw EnumException.of(String.format("ShardingStrategyType not support type: %s", type));
        }
        return strategyType;
    }

    public static ShardingStrategyConfiguration buildStrategyConfiguration(String type, String param) {
        if (type == null) {
            return null;
        }

        param = param.trim();
        param = ObjectUtils.isEmpty(param) ? StringConstant.EMPTY_JSON_OBJECT : param;
        Map<String, Object> paramMap = JsonUtil.toMap(param);
        String shardingColumn = (String) Optional.ofNullable(paramMap.get(ShardingRuleStrategyConstants.SHARDING_COLUMN)).orElse(StringConstant.EMPTY);
        String shardingColumns = (String) Optional.ofNullable(paramMap.get(ShardingRuleStrategyConstants.SHARDING_COLUMNS)).orElse(StringConstant.EMPTY);
        String shardingAlgorithmName = (String) Optional.ofNullable(paramMap.get(ShardingRuleStrategyConstants.SHARDING_ALGORITHM_NAME)).orElse(StringConstant.EMPTY);

        ShardingStrategyType strategyType = getByType(type);
        switch (strategyType) {
            case COMPLEX:
                return new ComplexShardingStrategyConfiguration(shardingColumns, shardingAlgorithmName);
            case STANDARD:
                return new StandardShardingStrategyConfiguration(shardingColumn, shardingAlgorithmName);
            case HINT:
                return new HintShardingStrategyConfiguration(shardingAlgorithmName);
            case NONE:
                return new NoneShardingStrategyConfiguration();
            default:
                throw EnumException.of(String.format("ShardingStrategyType not support type: %s", type));
        }
    }
}
