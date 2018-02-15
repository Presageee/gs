package com.gs.sso.entity;

import lombok.Data;

/**
 * Created by linjuntan on 2018/2/15.
 * email: ljt1343@gmail.com
 */
@Data
public class User {
    private int id;

    private String passport;

    private String password;

    private String username;

    private String email;

    private String phone;

    private Long createTime;

    private String loginIp;

    private String lastLoginIp;

    private Long loginTime;

    private Long lastLoginTime;

    private Long lastLogoutTime;

    private Long sensitive;

    private Integer locked;

    private Integer type;




}
