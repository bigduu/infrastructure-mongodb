package com.bigduu.infrastructuremongodb.baseconfig.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import sun.security.util.SecurityConstants;

/**
 * @Author yihao
 * @Date 2020/2/19 14:05
 * @Version 1.0
 */
public class DaoAuthenticationConfig<T extends DaoAuthenticationConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private DaoAuthenticationFilter authFilter;

    public DaoAuthenticationConfig() {
        this.authFilter = new DaoAuthenticationFilter(SecurityConstants.DAO_AUTH_URL);
    }

    @Override
    public void configure(B http) throws Exception {
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        //不将认证后的context放入session
        authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
        DaoAuthenticationFilter filter = postProcess(authFilter);
        //指定Filter的位置
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    //设置成功的Handler，这个handler定义成Bean，所以从外面set进来
    public com.csjx.qszs.infrastructure.config.DaoAuthenticationConfig<T, B> initHandler(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        authFilter.setAuthenticationSuccessHandler(successHandler);
        //设置失败的Handler
        authFilter.setAuthenticationFailureHandler(new DaoAuthenticationFailureHandler());
        return this;
    }

}