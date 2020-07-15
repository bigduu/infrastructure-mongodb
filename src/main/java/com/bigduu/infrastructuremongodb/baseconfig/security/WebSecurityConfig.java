package com.bigduu.infrastructuremongodb.baseconfig.security;

import com.bigduu.infrastructuremongodb.basefilter.OptionsRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private OptionsRequestFilter optionsRequestFilter;


    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置所有的请求都需要认证
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/openId/**").permitAll()
                .antMatchers("/wechat/**").permitAll()
                .antMatchers("/wechat/platform").permitAll()
                .antMatchers("/wechat/callback").permitAll()
                .antMatchers("/wechat/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/members/**").permitAll()
                .antMatchers(HttpMethod.GET, "/roles").permitAll()
                .antMatchers(HttpMethod.GET, "/departments").permitAll()
                .anyRequest().authenticated();
//                .accessDecisionManager(accessDecisionManager(new PermitAllVoter()));
        // 系统使用JWT而不是Session  禁用Form登录
        http.formLogin().disable();
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
        // 添加自定义的用户密码认证过滤器配置
        http.apply(new DaoAuthenticationConfig<>()).initHandler(daoAuthenticationSuccessHandler, daoAuthenticationFailureHandler);
        // 添加自定义的微信认证过滤器配置
        http.apply(new WeChatAuthenticationConfig<>()).initHandler(weChatAuthenticationSuccessHandler, weChatAuthenticationFailureHandler);
        // 添加自定义的jwt认证过滤器配置
        http.apply(new JwtAuthenticationConfig<>()).initHandler(jwtAuthenticationSuccessHandler, jwtAuthenticationFailureHandler);

        // 配置自定义授权失败异常处理回调 匿名用户尝试访问被保护的资源时403错误的回调
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
        // 配置登出（注销）默认就是"/logout"
        http.logout()
                .logoutUrl("/logout")
                .addLogoutHandler(tokenClearLogoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler);

    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(weChatAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean("daoAuthenticationProvider")
    protected AuthenticationProvider daoAuthenticationProvider() {

        CusDaoAuthenticationProvider daoProvider = new CusDaoAuthenticationProvider();
        daoProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoProvider.setUserDetailsService(userDetailsService());
        return daoProvider;
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "OPTION"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("Authentication");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
