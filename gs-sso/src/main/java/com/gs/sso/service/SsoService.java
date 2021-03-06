package com.gs.sso.service;

import com.gs.sso.controller.bo.UserBo;
import org.springframework.http.server.reactive.ServerHttpResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * author: linjuntan
 * date: 2018/2/6
 */
public interface SsoService {
    void createUser(UserBo userBo);

    void updateUser(UserBo userBo);

    UserBo login(String passport, String password, HttpServletResponse response);

    void logout(String token);
}
