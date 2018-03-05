package com.gs.inner.sso.service;


import com.gs.inner.sso.controller.bo.InnerUserBo;
import com.gs.inner.sso.entity.Role;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * author: theonelee
 * date: 2018/3/1
 */
public interface InnerSsoService {
    void createUser(InnerUserBo userBo);

    void updateUser(InnerUserBo userBo);

    /**
     * 更新 innerUser role
     * @param passport
     * @param roleList
     */
    void updateUserRole(String passport, List<Role> roleList);

    InnerUserBo login(String passport, String password, HttpServletResponse response);

    void logout(String token);


}
