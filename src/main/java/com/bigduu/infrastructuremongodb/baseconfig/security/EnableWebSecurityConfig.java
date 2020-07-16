package com.bigduu.infrastructuremongodb.baseconfig.security;

import com.bigduu.infrastructuremongodb.baseconfig.decision.BaseDecision;
import com.bigduu.infrastructuremongodb.basefilter.OptionsRequestFilter;
import com.bigduu.infrastructuremongodb.basefilter.UserResourceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

public class EnableWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final OptionsRequestFilter optionsRequestFilter;

    private final ProtectedUrlsConfiguration urlsConfiguration;

    private final UserResourceFilter userResourceFilter;

    public EnableWebSecurityConfig(OptionsRequestFilter optionsRequestFilter, ProtectedUrlsConfiguration urlsConfiguration, UserResourceFilter userResourceFilter) {
        this.optionsRequestFilter = optionsRequestFilter;
        this.urlsConfiguration = urlsConfiguration;
        this.userResourceFilter = userResourceFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (urlsConfiguration.getNotProtectedUrls().isEmpty()) {
            http.authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/users/openId/**").permitAll()
                    .antMatchers("/wechat/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/members/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/roles").permitAll()
                    .antMatchers(HttpMethod.GET, "/departments").permitAll()
                    .anyRequest().authenticated();
        } else {
            String[] strings = urlsConfiguration.getNotProtectedUrls().toArray(new String[0]);
            http.authorizeRequests()
                    .regexMatchers(strings).permitAll()
                    .anyRequest().authenticated();
        }
        // 配置所有的请求都需要认证
        //.accessDecisionManager(accessDecisionManager(new PermitAllVoter()));
        // 系统使用JWT而不是Session  禁用Form登录
        http.formLogin().disable();
        http.addFilterAfter(userResourceFilter, SecurityContextHolderAwareRequestFilter.class);
        // 禁用Session 即通过前端传token到后台过滤器中验证是否存在访问权限
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用CSRF攻击
        http.csrf().disable();
        // 允许跨域
        http.cors();
        // 在所有的返回头里添加允许跨域头信息
        http.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                new Header("Access-control-Allow-Origin", "*"),
                new Header("Access-Control-Expose-Headers", "Authentication"))));
        // 在Cors过滤器后添加一个Option请求过滤器 当检测到OPTIONS请求时 直接返回允许的请求头
        http.addFilterAfter(optionsRequestFilter, CorsFilter.class);
    }
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new UnanimousBased(Arrays.asList(baseDecision()));
    }
    @Bean
    public BaseDecision baseDecision() {
        return new BaseDecision();
    }


}
