package com.gs.sso.utils;

import com.gs.sso.config.SsoConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * author: linjuntan
 * date: 2018/2/23
 */
public class CookieUtil {
    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (SsoConstant.SSO_TOKEN.equals(cookie.getName())) {
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
            if (SsoConstant.SSO_TOKEN.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
