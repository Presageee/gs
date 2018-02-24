package com.gs.sso.config;

/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
public class SsoConstant {
    public static final String SSO_TOKEN = "SSO_TOKEN";

    public static final String CACHE_SSO_TOKEN_KEY = "sso_%s";

    public static final String CACHE_SSO_LOGIN_KEY = "sso_login_%s";

    public static final long TOKEN_EXPIRE_TIME = 60 * 60 * 12;

    public static final long TOKEN_EXPIRE_HOURS = 12;

    public static final String NO_LOGIN = "noLogin";

    public static final String READ_CACHE_ERROR = "readCacheError";

    public static final String WRITE_CACHE_ERROR = "writeCacheError";
}
