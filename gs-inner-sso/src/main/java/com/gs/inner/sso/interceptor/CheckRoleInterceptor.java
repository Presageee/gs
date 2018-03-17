package com.gs.inner.sso.interceptor;

import com.alibaba.fastjson.JSON;
import com.gs.cache.CacheProxy;
import com.gs.common.annotation.CheckRole;
import com.gs.common.utils.CommonUtil;
import com.gs.core.web.mvc.exception.ErrorEntity;
import com.gs.core.web.mvc.interceptor.GsInterceptor;
import com.gs.inner.sso.config.InnerSsoErrorCode;
import com.gs.inner.sso.dao.RoleMapper;
import com.gs.inner.sso.entity.InnerUser;
import com.gs.inner.sso.entity.Role;
import com.gs.inner.sso.utils.CookieUtil;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gs.inner.sso.config.InnerSsoConstant.*;

/**
 * Created by Lee on 2018/3/17 0017.
 */
@Slf4j
@Component
public class CheckRoleInterceptor extends GsInterceptor{

    @Autowired
    private CacheProxy cacheProxy;

    @Autowired
    private RoleMapper roleMapper;

    private Map<String, ErrorEntity> errorMap;

    @PostConstruct
    public void init() {
        initErrorMap();
        initResourceCache();
    }

    /**
     * 提前缓存每个role可以访问的url
     */
    private void initResourceCache() {
        List<Role> roleList=roleMapper.getRoleList();
        for (Role r:roleList){
            cacheProxy.set(String.valueOf(r.getId()),r.getAccessUrl());
        }
    }

    @Override
    public Integer getOrder() {
        return 10;
    }

    @Override
    public String[] getPathPatterns() {
        return new String[] {"/inner/*"};
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(" >>> in check role interceptor");
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getBean().getClass()
                    .isAnnotationPresent(CheckRole.class)) {
                return doCheck(request, response);
            }
        } else {
            if (handler.getClass().isAnnotationPresent(CheckRole.class)) {
                return doCheck(request, response);
            }
        }

        return true;
    }

    /**
     * * 校验流程：
     * 1.查出该innerUser的roleId
     * 2.根据roleId从cache查出可以访问的url并存入set
     * 3.判断访问的url是否处于该set中
     * @param request
     * @param response
     * @return
     */
    private boolean doCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //0. get cookie
        String token = CookieUtil.getToken((HttpServletRequest) request);
        if (CommonUtil.isBlank(token)) {
            writeErrorMsg(response, errorMap.get(NO_LOGIN));
            return false;
        }

        //1. check login
        String key = String.format(CACHE_INNER_SSO_TOKEN_KEY, token);
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

        InnerUser user=null;
        //2. get innerUser and update token expried time
        try {
            user = JSON.parseObject(userStr, InnerUser.class);
            cacheProxy.expire(String.format(CACHE_INNER_SSO_TOKEN_KEY, user.getPassport()), (int) TOKEN_EXPIRE_TIME);
            cacheProxy.expire(key, (int) TOKEN_EXPIRE_TIME);
            updateCookie(request, response);
        } catch (Exception e) {
            log.error(" >>> write cache error", e);
            writeErrorMsg(response, errorMap.get(WRITE_CACHE_ERROR));
            return false;
        }

        //3.get accessUrl
        String accessUrlStr=cacheProxy.get(String.valueOf(user.getRoleId()));
        String[] accessUrlList=accessUrlStr.split(",");

        //4.check url
        String uri = ((HttpServletRequest) request).getRequestURI();
        boolean res=Arrays.asList(accessUrlList).stream().anyMatch(e -> e.startsWith(uri));
        if (!res){
            writeErrorMsg(response, errorMap.get(NO_PERMISSION));
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

        ErrorEntity noLogin = new ErrorEntity(InnerSsoErrorCode.NO_LOGIN.getVal(), InnerSsoErrorCode.NO_LOGIN.name());
        ErrorEntity readCacheError = new ErrorEntity(InnerSsoErrorCode.READ_CACHE_ERROR.getVal(), InnerSsoErrorCode.READ_CACHE_ERROR.name());
        ErrorEntity writeCacheError = new ErrorEntity(InnerSsoErrorCode.SET_CACHE_ERROR.getVal(), InnerSsoErrorCode.SET_CACHE_ERROR.name());
        ErrorEntity noPermission = new ErrorEntity(InnerSsoErrorCode.NO_PERMISSION.getVal(), InnerSsoErrorCode.NO_LOGIN.name());

        errorMap.put(NO_LOGIN, noLogin);
        errorMap.put(NO_PERMISSION, noPermission);
        errorMap.put(READ_CACHE_ERROR, readCacheError);
        errorMap.put(WRITE_CACHE_ERROR, writeCacheError);
    }
}
