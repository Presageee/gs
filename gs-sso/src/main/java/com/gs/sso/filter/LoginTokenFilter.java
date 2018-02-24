package com.gs.sso.filter;

import com.alibaba.fastjson.JSON;
import com.gs.cache.CacheProxy;
import com.gs.common.utils.CommonUtil;
import com.gs.core.web.mvc.exception.ErrorEntity;
import com.gs.sso.config.SsoErrorCode;
import com.gs.sso.entity.User;
import com.gs.sso.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.gs.sso.config.SsoConstant.*;

/**
 * author: linjuntan
 * date: 2018/2/23
 */
@Slf4j
@Component
public class LoginTokenFilter implements Filter {
    private static final String CACHE_FILTER_URI_KEY = "loginToken_filter_uri";

    @Autowired
    private CacheProxy cacheProxy;

    private Map<String, ErrorEntity> errorMap;

    private ScheduledExecutorService service;

    @Value("#{'${server.filter.uri:/login,/logout,/user}'.split(',')}")
    private List<String> filterUris;

    @PostConstruct
    public void initFilter() {
        initErrorMap();
        initScheduleTask();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(" >>> login token filter start");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (containUri(request)) {
            chain.doFilter(request, response);
            return;
        }

        //0. get cookie
        String token = CookieUtil.getToken((HttpServletRequest) request);
        if (CommonUtil.isBlank(token)) {
            writeErrorMsg(response, errorMap.get(NO_LOGIN));
            return;
        }

        //1. check login
        String key = String.format(CACHE_SSO_TOKEN_KEY, token);
        String userStr;
        try {
            userStr = cacheProxy.get(key);
        } catch (Exception e) {
            log.error(" >>> read cache error", e);
            writeErrorMsg(response, errorMap.get(READ_CACHE_ERROR));
            return;
        }

        if (CommonUtil.isBlank(userStr)) {
            writeErrorMsg(response, errorMap.get(NO_LOGIN));
            return;
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
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info(" >>> destroy login token filter");
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

    private void writeErrorMsg(ServletResponse servletResponse, ErrorEntity errorEntity) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        response.getWriter().write(JSON.toJSONString(errorEntity));
    }

    private void updateCookie(ServletRequest request, ServletResponse response) {
        Cookie cookie = CookieUtil.getTokenCookie((HttpServletRequest) request);
        cookie.setMaxAge((int) TOKEN_EXPIRE_TIME);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setDomain("");

        ((HttpServletResponse) response).addCookie(cookie);
    }

    private boolean containUri(ServletRequest request) {
        String uri = ((HttpServletRequest) request).getRequestURI();
        return filterUris.stream().anyMatch(e -> e.startsWith(uri));
    }


    private void updateFilterUri() {
        String filterUri = cacheProxy.get(CACHE_FILTER_URI_KEY);
        if (CommonUtil.isBlank(filterUri)) {
            return;
        }

        filterUris = Arrays.asList(filterUri.split(","));
    }

    private void initScheduleTask() {
        if (service == null) {
            service = new ScheduledThreadPoolExecutor(1);
            service.scheduleAtFixedRate(this::updateFilterUri, 0, 60, TimeUnit.SECONDS);
        }
    }
}
