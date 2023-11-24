package com.jyx.feature.test.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author jiangyaxin
 * @since 2023/10/18 18:11
 */
@Configuration
@MapperScan(basePackages = DataSourceConfig2.Constants.MAPPER_PACKAGE, sqlSessionFactoryRef = DataSourceConfig2.Constants.SQL_SESSION_FACTORY_BEAN_NAME)
public class DataSourceConfig2 {

    interface Constants {
        String MAPPER_PACKAGE = "com.jyx.feature.test.mybatis.repository.repo2";

        String PROPERTIES_PREFIX_OF_YML = "spring.datasource.test2";

        String DATASOURCE = "jpaTest2";

        String ENTITY_PACKAGE = "com.jyx.feature.test.mybatis.domain.entity";

        String MAPPER_CLASS_PATH = "classpath:mapper/repo2/*.xml";

        String DATASOURCE_BEAN_NAME = DATASOURCE + "DataSource";

        String SQL_SESSION_FACTORY_BEAN_NAME = DATASOURCE + "SqlSessionFactory";

        String TRANSACTION_MANAGER_BEAN_NAME = DATASOURCE + "TransactionManager";

    }

    @Bean(name = DataSourceConfig2.Constants.DATASOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = DataSourceConfig2.Constants.PROPERTIES_PREFIX_OF_YML)
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = DataSourceConfig2.Constants.SQL_SESSION_FACTORY_BEAN_NAME)
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DataSourceConfig2.Constants.DATASOURCE_BEAN_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(DataSourceConfig2.Constants.ENTITY_PACKAGE);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceConfig2.Constants.MAPPER_CLASS_PATH));
        return bean.getObject();
    }

    @Bean(name = DataSourceConfig2.Constants.TRANSACTION_MANAGER_BEAN_NAME)
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier(DataSourceConfig2.Constants.DATASOURCE_BEAN_NAME) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
