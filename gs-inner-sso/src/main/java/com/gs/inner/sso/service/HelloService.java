package com.gs.inner.sso.service;

import com.gs.inner.sso.entity.InnerUser;
import com.gs.inner.sso.entity.Role;


/**
 * 测试自己写的User和Role的方法是否能运作
 * author: theonelee
 * date: 2018/2/28
 */
public interface HelloService {

    void createUser(InnerUser innerUser);

    void updateUser(InnerUser innerUser);

    InnerUser getUserById(Integer id);

    void createRole(Role role);

    Role getRoleById(Integer id);

    InnerUser getRoleListByUserId(Integer id);
}
