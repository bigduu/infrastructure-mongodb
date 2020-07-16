package com.bigduu.infrastructuremongodb.baseadvice;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mugeng.du
 */

public enum HttpCode {
    // 常规成功
    SUCCESS(0),

    // 常规失败
    FAIL(1),

    // 常规错误 带消息提示
    FAIL_MSG(2),

    // 身份认证失败
    AUTHENTICATION_FAILED(1001),

    // 身份信息失效
    AUTHENTICATION_EXPIRED(1002),

    // 账号已禁用
    ACCOUNT_BLOCKED(1003),

    // 账号未审核
    ACCOUNT_UNCHENKED(1004),

    // 账号审核未通过
    ACCOUNT_REFUSED(1005),

    // 微信未绑定账号
    OPENID_NOT_FOUND(1006);


    private Integer code;

    HttpCode(Integer code) {
        this.code = code;
    }

    @JsonValue
    public Integer getCode() {
        return this.code;
    }
}
