package com.gs.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.gs.cache.CacheProxy;
import com.gs.common.util.CommonUtils;
import com.gs.common.util.Cookie;
import com.gs.common.util.MD5Utils;
import com.gs.common.util.ReflectUtils;
import com.gs.core.web.exception.BaseWebException;
import com.gs.sso.controller.bo.UserBo;
import com.gs.sso.dao.UserMapper;
import com.gs.sso.entity.User;
import com.gs.sso.service.SsoService;
import com.sun.org.apache.bcel.internal.generic.FADD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;

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

    @Override
    public void createUser(UserBo userBo) {
        if (CommonUtils.isNull(userBo)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        User user = new User();
        user.setPassword(MD5Utils.encode(user.getPassword()));
        ReflectUtils.convertObject(userBo, user);

        userMapper.insertUser(user);
    }

    @Override
    public void updateUser(UserBo userBo) {
        if (CommonUtils.isNull(userBo)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        User user = new User();
        ReflectUtils.convertObject(userBo, user);

        userMapper.updateUser(user);
    }

    @Override
    public UserBo login(String passport, String password, ServerHttpResponse response) {
        if (CommonUtils.isBlank(passport, password)) {
            throw new BaseWebException(ARGS_IS_NULL.getVal(), ARGS_IS_NULL.name());
        }

        User user = userMapper.getUserByPassport(passport, MD5Utils.encode(password));
        if (CommonUtils.isNull(user)) {
            log.error(" >>> passport and password not match");
            throw new BaseWebException(USER_NOT_EXISTS.getVal(), USER_NOT_EXISTS.name());
        }

        String token = CommonUtils.getUUID();

        String ret = cacheProxy.setex(String.format(CACHE_SSO_TOKEN_KEY, token), (int) TOKEN_EXPIRE_TIME, JSON.toJSONString(user));

        if (CommonUtils.isNull(ret)) {
            log.warn(" >>> save token to cache error, user => {}", user.toString());
            throw new BaseWebException(SET_CACHE_ERROR.getVal(), SET_CACHE_ERROR.name());
        }

        UserBo userBo = new UserBo();
        ReflectUtils.convertObject(user, userBo);

        userBo.setToken(token);

        ResponseCookie cookie = ResponseCookie.from(SSO_TOKEN, token).domain("")
                .maxAge(Duration.ofHours(TOKEN_EXPIRE_HOURS))
                .path("/")
                .secure(false)
                .httpOnly(true).build();

        response.addCookie(cookie);

        return userBo;
    }

    @Override
    public void logout(String token) {
        Long ret = cacheProxy.del(String.format(CACHE_SSO_TOKEN_KEY, token));
        if (ret == null) {
            //todo throw del error exception
            log.warn(" >>> user logout error, token => {}", token);
            throw new BaseWebException(LOGOUT_ERROR.getVal(), LOGOUT_ERROR.name());
        }
        log.info(" >>> user logout, token => {}", token);
    }
}
