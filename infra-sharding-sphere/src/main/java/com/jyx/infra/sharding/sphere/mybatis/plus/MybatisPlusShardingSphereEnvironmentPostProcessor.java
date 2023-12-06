package com.jyx.infra.sharding.sphere.mybatis.plus;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.jyx.infra.sharding.sphere.InfraShardingSphereAutoConfig;
import com.jyx.infra.util.MapUtil;
import org.apache.commons.logging.Log;
import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author jiangyaxin
 * @since 2023/12/4 17:47
 */
@ConditionalOnClass(value = {DynamicDataSourceAutoConfiguration.class})
public class MybatisPlusShardingSphereEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    interface Constants {
        String EXCLUDE_CONFIG_SOURCE_NAME = "sharding.sphere.autoconfigure.exclude";

        String SPRING_AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";

        String EXCLUDE_AUTOCONFIGURE_CLASS = ShardingSphereAutoConfiguration.class.getName();
    }

    private final Log log;

    public MybatisPlusShardingSphereEnvironmentPostProcessor(DeferredLogFactory deferredLogFactory) {
        this.log = deferredLogFactory.getLog(InfraShardingSphereAutoConfig.class);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!environment.getPropertySources().contains(Constants.EXCLUDE_CONFIG_SOURCE_NAME)) {

            String existingExclusions = environment.getProperty(Constants.SPRING_AUTOCONFIGURE_EXCLUDE);
            String exclude = StringUtils.hasText(existingExclusions) ?
                    existingExclusions + "," + Constants.EXCLUDE_AUTOCONFIGURE_CLASS
                    : Constants.EXCLUDE_AUTOCONFIGURE_CLASS;

            Map<String, Object> properties = MapUtil.<String, Object>builder()
                    .put(Constants.SPRING_AUTOCONFIGURE_EXCLUDE, exclude)
                    .build();
            MapPropertySource propertySource = new MapPropertySource(Constants.EXCLUDE_CONFIG_SOURCE_NAME, properties);
            environment.getPropertySources().addFirst(propertySource);

            log.info(String.format("Add First PropertySource : %s=%s", propertySource.getName(),propertySource.getSource()));
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
