package com.gs.inner.sso.controller.bo;

import com.gs.common.annotation.Date;
import com.gs.inner.sso.entity.Role;
import lombok.Data;

import java.util.List;


/**
 * author: theonelee
 * date: 2018/3/1
 */
@Data
public class InnerUserBo {
    private Integer id;

    private String passport;

    private String password;

    private String username;

    private String email;

    private String phone;

    @Date
    private String createTime;

    private String loginIp;

    private String lastLoginIp;

    @Date
    private String loginTime;

    @Date
    private String lastLoginTime;

    @Date
    private String lastLogoutTime;

    private Integer locked;

    private Integer type;

    private Integer roleId;

    private Role role;

    private String token;

}
