package com.bigduu.infrastructuremongodb.baseexception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author yihao
 * @Date 2020/2/23 11:46
 * @Version 1.0
 * @description 账号状态为待审核时抛出此异常
 */
public class UnCheckedException extends AuthenticationException {
    public UnCheckedException(String msg, Throwable t) {
        super(msg, t);
    }

    public UnCheckedException(String msg) {
        super(msg);
    }
}
