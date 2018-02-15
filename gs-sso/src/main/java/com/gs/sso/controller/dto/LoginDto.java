package com.gs.sso.controller.dto;

import lombok.Data;

/**
 * Created by linjuntan on 2018/2/15.
 * email: ljt1343@gmail.com
 */
@Data
public class LoginDto {
    private String passport;

    private String password;

    private String captcha;
}
