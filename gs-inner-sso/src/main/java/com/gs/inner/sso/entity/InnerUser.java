package com.gs.inner.sso.entity;

import com.gs.common.annotation.Timestamp;
import lombok.Data;

import java.util.List;

/**
 * author: theonelee
 * date: 2018/3/6
 */
@Data
public class InnerUser {
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

    //该用户具备的角色
    private Role role;
}
