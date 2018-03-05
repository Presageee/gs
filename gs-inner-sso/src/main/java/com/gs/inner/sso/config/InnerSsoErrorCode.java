package com.gs.inner.sso.config;


/**
 * author: theonelee
 * date: 2018/3/1
 */
public enum InnerSsoErrorCode {
    ARGS_IS_NULL("1001"), LOGOUT_ERROR("1002"), SET_CACHE_ERROR("1003"), READ_CACHE_ERROR("1004"), PASSPORT_OR_PASSWORD_ERROR("1005"), NO_LOGIN("1006"),NO_PERMISSION("1007"),DATARESULT_IS_NULL("1008");

    InnerSsoErrorCode(String val) {
        this.val = val;
    }

    private String val;

    public String getVal() {
        return val;
    }
}
