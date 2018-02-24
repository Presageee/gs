package com.gs.sso.config;

/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
public enum SsoErrorCode {
    ARGS_IS_NULL("1001"), LOGOUT_ERROR("1002"), SET_CACHE_ERROR("1003"), READ_CACHE_ERROR("1004"), PASSPORT_OR_PASSWORD_ERROR("1005"), NO_LOGIN("1006");

    SsoErrorCode(String val) {
        this.val = val;
    }

    private String val;

    public String getVal() {
        return val;
    }
}
