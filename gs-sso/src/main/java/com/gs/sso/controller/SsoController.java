package com.gs.sso.controller;

import com.gs.sso.constant.SsoConstant;
import com.gs.sso.controller.bo.UserBo;
import com.gs.sso.controller.dto.LoginDto;
import com.gs.sso.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author: linjuntan
 * date: 2018/2/6
 */
@RestController
public class SsoController {

    @Autowired
    private SsoService ssoService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@RequestBody UserBo bo) {
        ssoService.createUser(bo);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<Void> modifyUser(@RequestBody UserBo bo) {
        ssoService.updateUser(bo);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping(name = "/login")
    public ResponseEntity<UserBo> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        UserBo userBo = ssoService.login(dto.getPassport(), dto.getPassword(), response);
        return new ResponseEntity<>(userBo, HttpStatus.OK);
    }

    @PutMapping("/logout")
    public ResponseEntity<Void> logout() {
        ssoService.logout(getToken());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private String getToken() {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (SsoConstant.SSO_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
