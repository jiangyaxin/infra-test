package com.jyx.feature.test.oauth2.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * WebAsyncManagerIntegrationFilter
 * SecurityContextPersistenceFilter
 * HeaderWriterFilter
 * LogoutFilter
 * OAuth2AuthorizationRequestRedirectFilter
 * OAuth2LoginAuthenticationFilter
 * DefaultLoginPageGeneratingFilter
 * DefaultLogoutPageGeneratingFilter
 * RequestCacheAwareFilter
 * SecurityContextHolderAwareRequestFilter
 * AnonymousAuthenticationFilter
 * SessionManagementFilter
 * ExceptionTranslationFilter
 * FilterSecurityInterceptor
 *
 * @author JYX
 * @since 2021/11/24 16:45
 */
@Configuration
public class Oauth2LoginConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/index.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
    }
}
