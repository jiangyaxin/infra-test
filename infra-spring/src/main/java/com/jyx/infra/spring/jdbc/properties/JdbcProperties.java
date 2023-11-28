package com.jyx.infra.spring.jdbc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2023/11/20 15:45
 */
@Data
@Component
@ConfigurationProperties(prefix = JdbcProperties.Constants.PROPERTIES_PREFIX)
public class JdbcProperties {

    interface Constants {

        String PROPERTIES_PREFIX = "jdbc";
    }

    private QueryProperties query = new QueryProperties();

    private InsertProperties insert = new InsertProperties();

}
