package com.bigduu.infrastructuremongodb.baseexception;


import org.springframework.security.core.AuthenticationException;

/**
 * @Author YiHao
 * @Date 3/10/2020 5:08 PM
 * @Description
 */
public class OpenIdNotFoundException extends AuthenticationException {


    public OpenIdNotFoundException(String message) {
        super(message);
    }


}
