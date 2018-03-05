package com.gs.sso.interceptor;

import com.alibaba.fastjson.JSON;
import com.gs.cache.CacheProxy;
import com.gs.common.annotation.CheckLogin;
import com.gs.common.utils.CommonUtil;
import com.gs.core.web.mvc.exception.ErrorEntity;
import com.gs.core.web.mvc.interceptor.GsInterceptor;
import com.gs.sso.config.SsoErrorCode;
import com.gs.sso.entity.User;
import com.gs.sso.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.gs.sso.config.SsoConstant.*;

/**
 * author: linjuntan
 * date: 2018/2/24
 */
@Slf4j
@Component
public class ChcekLoginInterceptor extends GsInterceptor {
    @Autowired
    private CacheProxy cacheProxy;

    private Map<String, ErrorEntity> errorMap;

    @PostConstruct
    public void init() {
        initErrorMap();
    }

    @Override
    public Integer getOrder() {
        return 10;
    }

    @Override
    public String[] getPathPatterns() {
        return new String[] {"/*"};
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(" >>> in check login interceptor");
        if (handler instanceof HandlerMethod) {//todo 这部分判断的意义？method.getBean().getClass()和handler.getClass()区别在哪？
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getBean().getClass()
                    .isAnnotationPresent(CheckLogin.class)) {
                return doCheck(request, response);
            }
        } else {
            if (handler.getClass().isAnnotationPresent(CheckLogin.class)) {
                return doCheck(request, response);
            }
        }

        return true;
    }

    private boolean doCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //0. get cookie
        String token = CookieUtil.getToken(request);
        if (CommonUtil.isBlank(token)) {
            writeErrorMsg(response, errorMap.get(NO_LOGIN));
            return false;
        }

        //1. check login
        String key = String.format(CACHE_SSO_TOKEN_KEY, token);
        String userStr;
        try {
            userStr = cacheProxy.get(key);
        } catch (Exception e) {
            log.error(" >>> read cache error", e);
            writeErrorMsg(response, errorMap.get(READ_CACHE_ERROR));
            return false;
        }

        if (CommonUtil.isBlank(userStr)) {
            writeErrorMsg(response, errorMap.get(NO_LOGIN));
            return false;
        }

        //2. update token expried time
        try {
            User user = JSON.parseObject(userStr, User.class);
            cacheProxy.expire(String.format(CACHE_SSO_LOGIN_KEY, user.getPassport()), (int) TOKEN_EXPIRE_TIME);
            cacheProxy.expire(key, (int) TOKEN_EXPIRE_TIME);
            updateCookie(request, response);
        } catch (Exception e) {
            log.error(" >>> write cache error", e);
            writeErrorMsg(response, errorMap.get(WRITE_CACHE_ERROR));
            return false;
        }

        return true;
    }

    private void writeErrorMsg(HttpServletResponse response, ErrorEntity errorEntity) throws IOException {
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        response.getWriter().write(JSON.toJSONString(errorEntity));
    }

    private void updateCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getTokenCookie(request);
        cookie.setMaxAge((int) TOKEN_EXPIRE_TIME);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setDomain("");

        response.addCookie(cookie);
    }

    private void initErrorMap() {
        errorMap = new HashMap<>(3);

        ErrorEntity noLogin = new ErrorEntity(SsoErrorCode.NO_LOGIN.getVal(), SsoErrorCode.NO_LOGIN.name());
        ErrorEntity readCacheError = new ErrorEntity(SsoErrorCode.READ_CACHE_ERROR.getVal(), SsoErrorCode.READ_CACHE_ERROR.name());
        ErrorEntity writeCacheError = new ErrorEntity(SsoErrorCode.SET_CACHE_ERROR.getVal(), SsoErrorCode.SET_CACHE_ERROR.name());

        errorMap.put(NO_LOGIN, noLogin);
        errorMap.put(READ_CACHE_ERROR, readCacheError);
        errorMap.put(WRITE_CACHE_ERROR, writeCacheError);
    }
}
