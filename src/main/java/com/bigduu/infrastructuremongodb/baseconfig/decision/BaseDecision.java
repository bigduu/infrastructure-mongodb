package com.bigduu.infrastructuremongodb.baseconfig.decision;

import com.bigduu.infrastructuremongodb.baseconfig.security.ProtectedUrlsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * @author bigduu
 * @title: BaseDecision
 * @projectName infrastructure-mongodb
 * @description: TODO
 * @date 2020/7/1623:53
 */
@Slf4j
public class BaseDecision implements AccessDecisionVoter<FilterInvocation> {


    @Autowired
    private ProtectedUrlsConfiguration protectedUrlsConfiguration;

    @Override
    public boolean supports(ConfigAttribute arg0) {
        return true;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public boolean supports(Class arg0) {
        return true;
    }

    @Autowired
    private ApplicationContext appContext;

    @SuppressWarnings("rawtypes")
    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection attr) {

        log.trace("RbacPermissionBasedAccessDecision is invoked with authentication: {}. Protected Urls: {}",
                authentication.getName(), protectedUrlsConfiguration.getNotProtectedUrls());


        String requestUrl = filterInvocation.getRequestUrl();
        if (isProtected(requestUrl)) {
            log.trace("Checking the authorization of URL: {}", requestUrl);
            return AccessDecisionVoter.ACCESS_DENIED;
        } else {
            log.debug("{} is not protected. Access is granted.", requestUrl);
            return AccessDecisionVoter.ACCESS_GRANTED;
        }
    }

    private boolean isProtected(String requestedUrl) {
        boolean isProtected = protectedUrlsConfiguration.getNotProtectedUrls().stream().anyMatch(requestedUrl::matches);

        log.trace("Is protected ({}): {}", requestedUrl, isProtected);

        return isProtected;
    }

}
