package com.jyx.infra.mybatis.plus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.jyx.infra.spring.jdbc.JdbcExecutor;
import com.jyx.infra.spring.jdbc.mysql.ReactMySqlJdbcExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangyaxin
 * @since 2023/10/25 14:56
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public JdbcExecutor jdbcExecutor() {
        return new ReactMySqlJdbcExecutor();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
