package com.gs.sso.dao;

import com.gs.sso.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * author: linjuntan
 * date: 2018/2/6
 */
public interface UserMapper {
    /**
     * 根据账号密码获取用户信息
     * @param passport 账号
     * @param password 密码
     * @return detail
     */
    User getUserByPassport(@Param("passport") String passport, @Param("password") String password);

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
     * 新建用户
     * @param user 信息
     */
    void insertUser(@Param("user") User user);

    /**
     * 修改用户信息
     * @param user 信息
     */
    void updateUser(@Param("user") User user);
}
