package com.gs.inner.sso.config;


/**
 * author: theonelee
 * date: 2018/3/1
 */
public class InnerSsoConstant {
    public static final String INNER_SSO_TOKEN = "INNER_SSO_TOKEN";

    public static final String CACHE_INNER_SSO_TOKEN_KEY = "inner_sso_%s";

    public static final String CACHE_INNER_SSO_LOGIN_KEY = "inner_sso_login_%s";

    public static final long TOKEN_EXPIRE_TIME = 60 * 60 * 12;

    public static final long TOKEN_EXPIRE_HOURS = 12;

    public static final String NO_LOGIN = "noLogin";

    public static final String NO_PERMISSION = "noPermission";

    public static final String READ_CACHE_ERROR = "readCacheError";

    public static final String WRITE_CACHE_ERROR = "writeCacheError";
}
