package com.jyx.infra.mybatis.plus.generator.properties;

import lombok.Data;

import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/10/29 15:25
 */
@Data
public class StrategyProperties {

    private boolean fileOverride;

    private List<String> includes;

    private String superEntityClass;
}
