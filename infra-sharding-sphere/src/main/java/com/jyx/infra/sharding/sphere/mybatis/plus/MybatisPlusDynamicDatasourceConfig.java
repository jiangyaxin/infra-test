package com.jyx.infra.sharding.sphere.mybatis.plus;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author jiangyaxin
 * @since 2023/12/4 16:12
 */
@ConditionalOnClass(value = {DynamicDataSourceAutoConfiguration.class})
@AutoConfigureBefore(value = {DynamicDataSourceAutoConfiguration.class})
@RequiredArgsConstructor
public class MybatisPlusDynamicDatasourceConfig {

    private final DynamicDataSourceProperties properties;

    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }
}
