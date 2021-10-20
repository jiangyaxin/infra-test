package com.jyx.infra.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author JYX
 * @since 2021/10/20 16:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppContext {

    private ClusterContext cluster;

    private boolean enabledCachedIdGenerator;

}
