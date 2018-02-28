package com.gs.inner.sso.dao;

import com.gs.inner.sso.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * author: theonelee
 * date: 2018/2/28
 */
public interface UserMapper {
    /**
     * 根据账号密码获取用户信息
     * @param passport 账号
     * @param password 密码
     * @return detail
     */
    User getUserByPassportAndPassword(@Param("passport") String passport, @Param("password") String password);

    /**
     * 根据账号获取用户信息
     * @param passport 根据账号获取用户信息
     * @return
     */
    User getUserByPassport(@Param("passport") String passport);

    /**
     * 根据id获取用户信息
     * @param id id
     * @return detail
     */
    User getUserById(@Param("id") int id);

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
     * @param user 信息
     */
    void insertUser(@Param("user") User user);

    /**
     * 修改用户信息
     * @param user 信息
     */
    void updateUser(@Param("user") User user);

    /**
     * 更新登录信息
     * @param user
     */
    void updateInfo(@Param("user") User user);

    /**
     * 更新登出时间
     * @param time
     * @param passport
     */
    void updateLogoutTime(@Param("logoutTime") Long time, @Param("passport") String passport);

    /**
     * 修改用户角色
     * @param user
     */
    void updateUserRole(@Param("user") User user);
}
