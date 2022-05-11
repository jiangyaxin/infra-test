package com.jyx.feature.test.oauth2.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 1. 当FilterSecurityInterceptor鉴权失败时，由ExceptionTranslationFilter使用AuthenticationEntryPoint重定向到/oauth2/authorization/{registrationId}。
 *
 * 2. 当requet可以匹配到DEFAULT_AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization/{registrationId}"时，
 *    OAuth2AuthorizationRequestRedirectFilter会根据registrationId获得对应的ClientRegistration，
 *    然后构造出OAuth2AuthorizationRequest重定向到authorizationUri。
 *
 * 3. 填写用户名密码，认证通过Authorization Server回调备案地址(如上面例子中http://localhost:8080/login/oauth2/code/google)并携带code
 *    参数，备案地址需要匹配AbstractAuthenticationProcessingFilter中requiresAuthenticationRequestMatcher，
 *    可以通过defaultFilterProcessesUrl修改，默认值为DEFAULT_FILTER_PROCESSES_URI = "/login/oauth2/code/*"。
 *
 *    为了更好的与前端进行交互，可以跳转到一个中间页面，由中间页面发起http://localhost:8080/login/oauth2/code/google并传入code参数，
 *    然后后台返回token和用户之前所在的页面，然后由中间页面保存token到storage,再跳转到用户想要访问的页面，这样就可以不使用cookie而使用token 访问。
 *
 * 4. 由OAuth2LoginAuthenticationFilter拦截到http://localhost:8080/login/oauth2/code/google开始POST调用tokenUri并携带上一步得到的code参数，
 *    获取到access_token后重定向回最开始用户想访问的URL并设置cookie。
 *
 * 5. 然后带着cookie访问。
 *
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
                .csrf(AbstractHttpConfigurer::disable)
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/index.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico");
    }
}
