package com.jyx.infra.spring.pipeline;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Archforce
 * @since 2023/11/3 15:21
 */
@Data
@Component
@ConditionalOnProperty(prefix = PipelineConfigProperties.Constants.PROPERTIES_PREFIX, name = PipelineConfigProperties.Constants.ENABLE)
@ConfigurationProperties(prefix = PipelineConfigProperties.Constants.PROPERTIES_PREFIX)
public class PipelineConfigProperties {

    interface Constants {
        String PROPERTIES_PREFIX = "pipeline";

        String ENABLE = "enable";
    }

    private Boolean enable;

    private Map<String, PipelineProperties> config;

}
