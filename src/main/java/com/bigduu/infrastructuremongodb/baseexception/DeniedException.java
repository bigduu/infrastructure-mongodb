package com.bigduu.infrastructuremongodb.baseexception;


import org.springframework.security.core.AuthenticationException;

/**
 * @Author yihao
 * @Date 2020/2/23 11:53
 * @Version 1.0
 * @Description 账号未通过审核时抛出此异常
 */
public class DeniedException extends AuthenticationException {
    public DeniedException(String msg, Throwable t) {
        super(msg, t);
    }

    public DeniedException(String msg) {
        super(msg);
    }
}
