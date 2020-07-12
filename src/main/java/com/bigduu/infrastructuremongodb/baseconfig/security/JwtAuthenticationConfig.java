package com.bigduu.infrastructuremongodb.baseconfig.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author yihao
 * @Date 2020/2/19 14:23
 * @Version 1.0
 */
public class JwtAuthenticationConfig<T extends JwtAuthenticationConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private JwtAuthenticationFilter authFilter;

    public JwtAuthenticationConfig() {
        this.authFilter = new JwtAuthenticationFilter();
    }

    @Override
    public void configure(B http) {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //将filter放到logoutFilter之前
        JwtAuthenticationFilter filter = postProcess(authFilter);
        http.addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }

    //设置匿名用户可访问url
    public com.csjx.qszs.infrastructure.config.JwtAuthenticationConfig<T, B> permissiveRequestUrls(String... urls) {
        authFilter.setPermissiveUrls(urls);
        return this;
    }

    public com.csjx.qszs.infrastructure.config.JwtAuthenticationConfig<T, B> initHandler(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        authFilter.setAuthenticationSuccessHandler(successHandler);
        authFilter.setAuthenticationFailureHandler(failureHandler);
        return this;
    }

}