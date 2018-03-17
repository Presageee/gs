package com.gs.inner.sso.service.impl;

import com.gs.inner.sso.dao.RoleMapper;
import com.gs.inner.sso.dao.InnerUserMapper;
import com.gs.inner.sso.entity.InnerUser;
import com.gs.inner.sso.entity.Role;
import com.gs.inner.sso.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author: theonelee
 * date: 2018/2/28
 */
@Service("helloService")
@Slf4j
public class HelloServiceImpl implements HelloService{

    @Autowired
    private InnerUserMapper innerUserMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void createUser(InnerUser innerUser) {
        innerUserMapper.insertUser(innerUser);
    }

    @Override
    public void updateUser(InnerUser innerUser) {
        innerUserMapper.updateUser(innerUser);
    }

    @Override
    public InnerUser getUserById(Integer id) {
        return innerUserMapper.getUserById(id);
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
