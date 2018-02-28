package com.gs.inner.sso.service.impl;

import com.gs.inner.sso.dao.RoleMapper;
import com.gs.inner.sso.dao.UserMapper;
import com.gs.inner.sso.entity.Role;
import com.gs.inner.sso.entity.User;
import com.gs.inner.sso.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author: theonelee
 * date: 2018/2/28
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void createUser(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void createRole(Role role) {
        roleMapper.insertRole(role);
    }

    @Override
    public Role getRoleById(Integer id) {
        return roleMapper.getRoleById(id);
    }
}
