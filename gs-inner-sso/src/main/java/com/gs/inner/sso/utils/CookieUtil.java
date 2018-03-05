package com.gs.inner.sso.utils;


import com.gs.inner.sso.config.InnerSsoConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * author: theonelee
 * date: 2018/3/1
 */
public class CookieUtil {
    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (InnerSsoConstant.INNER_SSO_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Cookie getTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (InnerSsoConstant.INNER_SSO_TOKEN.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
