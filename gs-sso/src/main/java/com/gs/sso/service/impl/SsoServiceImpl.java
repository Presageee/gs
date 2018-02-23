package com.gs.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.gs.cache.CacheProxy;
import com.gs.common.utils.CommonUtil;
import com.gs.common.utils.MD5Util;
import com.gs.common.utils.ReflectUtil;
import com.gs.core.web.mvc.exception.BaseWebException;
import com.gs.core.web.mvc.utils.RequestUtil;
import com.gs.sso.controller.bo.UserBo;
import com.gs.sso.dao.UserMapper;
import com.gs.sso.entity.User;
import com.gs.sso.service.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.gs.sso.constant.SsoConstant.*;
import static com.gs.sso.constant.SsoErrorCode.*;

/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
@Service("ssoService")
@Slf4j
public class SsoServiceImpl implements SsoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheProxy cacheProxy;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void createUser(UserBo userBo) {
        if (CommonUtil.isNull(userBo)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        User user = new User();
        ReflectUtil.convertObject(userBo, user);

        user.setPassword(MD5Util.encode(user.getPassword()));
        userMapper.insertUser(user);
    }

    @Override
    public void updateUser(UserBo userBo) {
        if (CommonUtil.isNull(userBo)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        if (CommonUtil.isBlank(userBo.getPassport())) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        User user = userMapper.getUserByPassport(userBo.getPassport());
        user.setUsername(userBo.getUsername());
        user.setEmail(userBo.getEmail());
        user.setPhone(userBo.getPhone());
        user.setLocked(userBo.getLocked());
        user.setType(userBo.getType());

        userMapper.updateUser(user);
    }

    @Override
    public UserBo login(String passport, String password, HttpServletResponse response) {
        if (CommonUtil.isBlank(passport, password)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        User user = userMapper.getUserByPassportAndPassword(passport, MD5Util.encode(password));
        if (CommonUtil.isNull(user)) {
            log.error(" >>> passport and password not match");
            throw new BaseWebException(USER_NOT_EXISTS.getVal(), USER_NOT_EXISTS.name());
        }

        String token = CommonUtil.getUUID();

        String ret = cacheProxy.setex(String.format(CACHE_SSO_TOKEN_KEY, token), (int) TOKEN_EXPIRE_TIME, JSON.toJSONString(user));

        if (CommonUtil.isNull(ret)) {
            log.warn(" >>> save token to cache error, user => {}", user.toString());
            throw new BaseWebException(SET_CACHE_ERROR.getVal(), SET_CACHE_ERROR.name());
        }

        UserBo userBo = new UserBo();
        ReflectUtil.convertObject(user, userBo);
        userBo.setToken(token);

        setLoginToken(token, response);
        updateLoginInfo(user);

        return userBo;
    }

    @Override
    public void logout(String token) {
        String key = String.format(CACHE_SSO_TOKEN_KEY, token);
        String userStr = cacheProxy.get(key);

        if (CommonUtil.isBlank(userStr)) {
            log.warn(" >>> user already logout, token => {}", token);
            return;
        }

        Long ret = cacheProxy.del(String.format(CACHE_SSO_TOKEN_KEY, token));
        if (ret == null) {
            //todo throw del error exception
            log.warn(" >>> user logout error, token => {}", token);
            throw new BaseWebException(LOGOUT_ERROR.getVal(), LOGOUT_ERROR.name());
        }
        User user = JSON.parseObject(userStr, User.class);
        userMapper.updateLogoutTime(System.currentTimeMillis(), user.getPassport());
        log.info(" >>> user logout, token => {}", token);
    }

    private void updateLoginInfo(User user) {
        user.setLastLoginIp(user.getLoginIp());
        user.setLastLoginTime(user.getLoginTime());
        user.setLoginTime(System.currentTimeMillis());
        user.setLoginIp(RequestUtil.getIpAddr(request));
        userMapper.updateInfo(user);
    }

    private void setLoginToken(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(SSO_TOKEN, token);
        cookie.setDomain("");
        cookie.setMaxAge((int) (TOKEN_EXPIRE_HOURS * 60 * 60));
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
}
