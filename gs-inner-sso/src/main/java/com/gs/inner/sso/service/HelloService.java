package com.gs.inner.sso.service;

import com.gs.inner.sso.entity.Role;
import com.gs.inner.sso.entity.User;

import java.util.List;


/**
 * 测试自己写的User和Role的方法是否能运作
 * author: theonelee
 * date: 2018/2/28
 */
public interface HelloService {

    void createUser(User user);

    void updateUser(User user);

    User getUserById(Integer id);

    void createRole(Role role);

    Role getRoleById(Integer id);

    User getRoleListByUserId(Integer id);
}
