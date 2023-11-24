package com.jyx.infra.mybatis.plus.generator.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2023/10/29 12:02
 */
@Data
@Component
@ConditionalOnProperty(prefix = MybatisPlusGeneratorProperties.Constants.PROPERTIES_PREFIX ,name = MybatisPlusGeneratorProperties.Constants.ENABLE)
@ConfigurationProperties(prefix = MybatisPlusGeneratorProperties.Constants.PROPERTIES_PREFIX)
public class MybatisPlusGeneratorProperties {

    interface Constants {
        String PROPERTIES_PREFIX = "mybatis-generator";

        String ENABLE = "enable";
    }

    private DataSourceProperties datasource;

    private GlobalProperties global;

    private PackageProperties packagePath;

    private StrategyProperties strategy;

}
