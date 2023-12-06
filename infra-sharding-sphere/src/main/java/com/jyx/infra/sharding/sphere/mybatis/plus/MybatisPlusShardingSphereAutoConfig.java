package com.jyx.infra.sharding.sphere.mybatis.plus;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.jyx.infra.sharding.sphere.ShardingSphereContants;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.infra.yaml.config.swapper.mode.YamlModeConfigurationSwapper;
import org.apache.shardingsphere.spring.boot.prop.SpringBootPropertiesConfiguration;
import org.apache.shardingsphere.spring.boot.rule.LocalRulesCondition;
import org.apache.shardingsphere.spring.boot.schema.DatabaseNameSetter;
import org.apache.shardingsphere.spring.transaction.TransactionTypeScanner;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author jiangyaxin
 * @since 2023/12/4 13:54
 */
@Configuration
@ConditionalOnClass(value = {DynamicDataSourceAutoConfiguration.class})
@ComponentScan("org.apache.shardingsphere.spring.boot.converter")
@EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@AutoConfigureAfter(value = {MybatisPlusDynamicDatasourceConfig.class, DynamicDataSourceAutoConfiguration.class})
public class MybatisPlusShardingSphereAutoConfig implements EnvironmentAware {

    private Environment environment;

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    private final SpringBootPropertiesConfiguration props;

    public MybatisPlusShardingSphereAutoConfig(SpringBootPropertiesConfiguration props, DataSource dataSource) {
        this.props = props;
        this.dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
    }

    @Bean
    public ModeConfiguration modeConfiguration() {
        return null == props.getMode() ? null : new YamlModeConfigurationSwapper().swapToObject(props.getMode());
    }


    @Bean
    @Conditional(LocalRulesCondition.class)
    @Autowired(required = false)
    public DataSource shardingSphereDataSource(final ObjectProvider<List<RuleConfiguration>> rules, final ObjectProvider<ModeConfiguration> modeConfig) throws SQLException {
        Collection<RuleConfiguration> ruleConfigs = Optional.ofNullable(rules.getIfAvailable()).orElseGet(Collections::emptyList);
        String databaseName = getDatabaseName();
        Map<String, DataSource> dataSourceMap = getDataSourceMap();
        DataSource dataSource = ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig.getIfAvailable(), dataSourceMap, ruleConfigs, props.getProps());
        dynamicRoutingDataSource.addDataSource(ShardingSphereContants.SHARDING_SPHERE_DS_NAME, dataSource);
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource(final ModeConfiguration modeConfig) throws SQLException {
        String databaseName = getDatabaseName();
        Map<String, DataSource> dataSourceMap = getDataSourceMap();
        DataSource dataSource = !dataSourceMap.isEmpty() ?
                ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig, dataSourceMap, Collections.emptyList(), props.getProps())
                : ShardingSphereDataSourceFactory.createDataSource(databaseName, modeConfig);
        dynamicRoutingDataSource.addDataSource(ShardingSphereContants.SHARDING_SPHERE_DS_NAME, dataSource);
        return dataSource;
    }

    @Bean
    public TransactionTypeScanner transactionTypeScanner() {
        return new TransactionTypeScanner();
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    private Map<String, DataSource> getDataSourceMap() {
        Map<String, DataSource> existMap = dynamicRoutingDataSource.getDataSources();
        return MybatisPlusDataSourceMapSetter.getDataSourceMap(environment, existMap);
    }

    private String getDatabaseName() {
        return DatabaseNameSetter.getDatabaseName(environment);
    }

}
