package com.gs.inner.sso.dao;

import com.gs.inner.sso.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: theonelee
 * date: 2018/2/28
 */
public interface RoleMapper {
    Role getRoleById(@Param("id") Integer id);

    List<Role> getRoleList();

    void deleteRoleById(@Param("id") Integer id);

    void updateRoleById(@Param("role") Role role);

    void insertRole(@Param("role") Role role);

}
