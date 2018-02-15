package com.gs.sso.controller.bo;

import com.gs.common.annotation.Date;
import lombok.Data;


/**
 * Created by linjuntan on 2018/2/15.
 * email: ljt1343@gmail.com
 */
@Data
public class UserBo {
    private int id;

    private String passport;

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

    private Long sensitive;

    private Integer locked;

    private Integer type;

}
