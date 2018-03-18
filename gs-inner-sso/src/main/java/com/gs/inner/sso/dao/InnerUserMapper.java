package com.gs.inner.sso.dao;

import com.gs.inner.sso.entity.InnerUser;
import com.gs.inner.sso.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: theonelee
 * date: 2018/2/28
 */
public interface InnerUserMapper {
    /**
     * 根据账号密码获取用户信息
     * @param passport 账号
     * @param password 密码
     * @return detail
     */
    InnerUser getUserByPassportAndPassword(@Param("passport") String passport, @Param("password") String password);

    /**
     * 根据账号获取用户信息
     * @param passport 根据账号获取用户信息
     * @return
     */
    InnerUser getUserByPassport(@Param("passport") String passport);

    /**
     * 根据id获取用户信息
     * @param id id
     * @return detail
     */
    InnerUser getUserById(@Param("id") int id);

    /**
     * 根据id获取用户角色List 且附带部分用户信息
     * @param id id
     * @return detail
     */
    InnerUser getRoleByUserId(@Param("id") int id);

    /**
     * 根据passport获取用户角色List 且附带部分用户信息
     * @param passport
     * @return detail
     */
    InnerUser getRoleByUserPassport(@Param("passport") String passport);

    /**
     * 根据账号获取密码
     * @param passport 账号
     * @return 密码
     */
    String getPasswordByPassport(@Param("passport") String passport);

    /**
     * 获取passport是否存在
     * @param passport passport
     * @return
     */
    Integer getCountByPassport(@Param("passport") String passport);

    /**
     * 新建用户
     * @param innerUser 信息
     */
    void insertUser(@Param("innerUser") InnerUser innerUser);

    /**
     * 修改用户信息
     * @param innerUser 信息
     */
    void updateUser(@Param("innerUser") InnerUser innerUser);

    /**
     * 更新登录信息
     * @param innerUser
     */
    void updateInfo(@Param("innerUser") InnerUser innerUser);

    /**
     * 更新登出时间
     * @param time
     * @param passport
     */
    void updateLogoutTime(@Param("logoutTime") Long time, @Param("passport") String passport);



    void getUserWithRoleById(@Param("id") int id);
}
