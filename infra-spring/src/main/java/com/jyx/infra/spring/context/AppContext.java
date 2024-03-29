package com.jyx.infra.spring.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2021/10/20 16:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppContext {

    private ClusterContext cluster = new ClusterContext();

    private PoolContext pool = new PoolContext();

}
