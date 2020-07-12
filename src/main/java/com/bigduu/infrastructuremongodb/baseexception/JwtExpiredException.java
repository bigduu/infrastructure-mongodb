package com.bigduu.infrastructuremongodb.baseexception;


import org.springframework.security.core.AuthenticationException;

/**
 * @Author yihao
 * @Date 2020/2/23 12:01
 * @Version 1.0
 */
public class JwtExpiredException extends AuthenticationException {
    public JwtExpiredException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtExpiredException(String msg) {
        super(msg);
    }
}
