package com.gs.inner.sso.controller.dto;

import lombok.Data;

/**
 * author: theonelee
 * date: 2018/3/1
 */
@Data
public class InnerLoginDto {
    private String passport;

    private String password;

    private String captcha;
}
