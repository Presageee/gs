package com.gs.sso.entity;

import com.gs.common.annotation.Timestamp;
import lombok.Data;

/**
 * Created by linjuntan on 2018/2/15.
 * email: ljt1343@gmail.com
 */
@Data
public class User {
    private Integer id;

    private String passport;

    private String password;

    private String username;

    private String email;

    private String phone;

    @Timestamp
    private Long createTime;

    private String loginIp;

    private String lastLoginIp;

    @Timestamp
    private Long loginTime;

    @Timestamp
    private Long lastLoginTime;

    @Timestamp
    private Long lastLogoutTime;

    private Integer locked;

    private Integer type;




}
