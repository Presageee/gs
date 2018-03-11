package com.gs.inner.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.gs.cache.CacheProxy;
import com.gs.common.utils.CommonUtil;
import com.gs.common.utils.MD5Util;
import com.gs.common.utils.ReflectUtil;
import com.gs.core.web.mvc.exception.BaseWebException;
import com.gs.core.web.mvc.utils.RequestUtil;
import com.gs.inner.sso.controller.bo.InnerUserBo;
import com.gs.inner.sso.dao.InnerUserMapper;
import com.gs.inner.sso.entity.InnerUser;
import com.gs.inner.sso.entity.Role;
import com.gs.inner.sso.service.InnerSsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.gs.inner.sso.config.InnerSsoConstant.*;
import static com.gs.inner.sso.config.InnerSsoErrorCode.*;

/**
 * author: theonelee
 * date: 2018/3/1
 */
@Slf4j
@Service("innerSsoService")
public class InnerSsoServiceImpl implements InnerSsoService{

    @Autowired
    private InnerUserMapper innerUserMapper;

    @Autowired
    private CacheProxy cacheProxy;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void createUser(InnerUserBo userBo) {
        if (CommonUtil.isNull(userBo)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        InnerUser user = new InnerUser();
        ReflectUtil.convertObject(userBo, user);

        user.setPassword(MD5Util.encode(user.getPassword()));
        innerUserMapper.insertUser(user);
    }

    @Override
    public void updateUser(InnerUserBo userBo) {
        if (CommonUtil.isNull(userBo)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        if (CommonUtil.isBlank(userBo.getPassport())) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        InnerUser user = innerUserMapper.getUserByPassport(userBo.getPassport());
        user.setUsername(userBo.getUsername());
        user.setEmail(userBo.getEmail());
        user.setPhone(userBo.getPhone());
        user.setLocked(userBo.getLocked());
        user.setType(userBo.getType());
        //user.setRoleList(userBo.getRoleList());


        innerUserMapper.updateUser(user);
    }

    @Override
    public void updateUserRole(String passport, List<Role> roleList) {
        if (CommonUtil.isNull(passport)||CommonUtil.isNull(roleList)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }
        int count=innerUserMapper.getCountByPassport(passport);
        if (count==0){
            throw new BaseWebException(DATARESULT_IS_NULL.getVal(), DATARESULT_IS_NULL.name());
        }

        innerUserMapper.updateUserRole(passport,roleList);
    }

    @Override
    public InnerUserBo login(String passport, String password, HttpServletResponse response) {
        if (CommonUtil.isBlank(passport, password)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        InnerUser user = innerUserMapper.getUserByPassportAndPassword(passport, MD5Util.encode(password));
        if (CommonUtil.isNull(user)) {
            log.error(" >>> inner user passport and password not match");
            throw new BaseWebException(PASSPORT_OR_PASSWORD_ERROR.getVal(), PASSPORT_OR_PASSWORD_ERROR.name());
        }

        String token = CommonUtil.getUUID();

        String loginKey = String.format(CACHE_INNER_SSO_LOGIN_KEY, user.getPassport());
        if (cacheProxy.exists(loginKey)) {
            String oldToken = cacheProxy.get(loginKey);
            //delete old login token
            cacheProxy.del(oldToken);
        }
        String newTokenKey = String.format(CACHE_INNER_SSO_TOKEN_KEY, token);

        String loginKeyRet = cacheProxy.setex(loginKey, (int) TOKEN_EXPIRE_TIME, newTokenKey);
        if (CommonUtil.isNull(loginKeyRet)) {
            log.warn(" >>> save token to cache error, inner user => {}", user.toString());
            throw new BaseWebException(SET_CACHE_ERROR.getVal(), SET_CACHE_ERROR.name());
        }

        String ret = cacheProxy.setex(newTokenKey,
                (int) TOKEN_EXPIRE_TIME, JSON.toJSONString(user));

        if (CommonUtil.isNull(ret)) {
            log.warn(" >>> save token to cache error, inner user => {}", user.toString());
            throw new BaseWebException(SET_CACHE_ERROR.getVal(), SET_CACHE_ERROR.name());
        }

        InnerUserBo userBo = new InnerUserBo();
        ReflectUtil.convertObject(user, userBo);
        userBo.setToken(token);

        setLoginToken(token, response);
        updateLoginInfo(user);

        return userBo;
    }

    @Override
    public void logout(String token) {
        String key = String.format(CACHE_INNER_SSO_TOKEN_KEY, token);
        String userStr = cacheProxy.get(key);

        if (CommonUtil.isBlank(userStr)) {
            log.warn(" >>> user already logout, token => {}", token);
            return;
        }

        Long ret = cacheProxy.del(String.format(CACHE_INNER_SSO_TOKEN_KEY, token));
        if (ret == null) {
            //todo throw del error exception
            log.warn(" >>> inner user logout error, token => {}", token);
            throw new BaseWebException(LOGOUT_ERROR.getVal(), LOGOUT_ERROR.name());
        }
        InnerUser user = JSON.parseObject(userStr, InnerUser.class);
        innerUserMapper.updateLogoutTime(System.currentTimeMillis(), user.getPassport());
        log.info(" >>> inner user logout, token => {}", token);
    }

    private void updateLoginInfo(InnerUser user) {
        user.setLastLoginIp(user.getLoginIp());
        user.setLastLoginTime(user.getLoginTime());
        user.setLoginTime(System.currentTimeMillis());
        user.setLoginIp(RequestUtil.getIpAddr(request));
        innerUserMapper.updateInfo(user);
    }

    private void setLoginToken(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(INNER_SSO_TOKEN, token);
        cookie.setDomain("");
        cookie.setMaxAge((int) (TOKEN_EXPIRE_HOURS * 60 * 60));
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
}
