package com.bigduu.infrastructuremongodb.basefilter;

import com.bigduu.infrastructuremongodb.baseconfig.security.ProtectedUrlsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author bigduu
 * @title: UserResourceFilter
 * @projectName infrastructure-mongodb
 * @description: TODO
 * @date 2020/7/1522:12
 */
@Slf4j
@Component
public class UserResourceFilter implements Filter {

    @Autowired
    private ProtectedUrlsConfiguration protectedUrlsConfiguration;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestMethod = request.getMethod();
        String requestUrl = request.getServletPath();
        String clientIp = getClientIp(request);
        log.info("user resource filter is invoked with url:[{}],(method:[{}]) from client:[{}].(configured protected Urls:{}", requestUrl, requestMethod, clientIp, protectedUrlsConfiguration.getNotProtectedUrls());
        if (protectedUrlsConfiguration.getNotProtectedUrls().stream().anyMatch(requestUrl::matches)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String username = authentication.getName();
                log.info("Invoked permission filter for [{}]",username);
            }
        }
        filterChain.doFilter(request, servletResponse);
        log.info("Completed request in [{}] millis",System.currentTimeMillis()-startTime);
    }

    private String getClientIp(HttpServletRequest httpServletRequest) {
        String clientIp = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isBlank(clientIp)) {
            clientIp = httpServletRequest.getRemoteAddr();
        }
        return clientIp;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
