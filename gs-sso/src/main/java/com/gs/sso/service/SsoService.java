package com.gs.sso.service;

import com.gs.sso.controller.bo.UserBo;

/**
 * author: linjuntan
 * date: 2018/2/6
 */
public interface SsoService {
    void createUser(UserBo userBo);

    void updateUser(UserBo userBo);

    UserBo login(String passport, String password);

    void logout(String token);
}
