package com.gs.sso.controller;

import com.gs.sso.controller.bo.UserBo;
import com.gs.sso.controller.dto.LoginDto;
import com.gs.sso.service.SsoService;
import com.gs.sso.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    private HttpServletRequest request;//todo 测试下客户端a登陆后将token给客户端b时，能否正常登陆

    @PostMapping(value = "/user")
    public ResponseEntity<Void> createUser(@RequestBody UserBo bo) {
        ssoService.createUser(bo);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(value = "/user")
    public ResponseEntity<Void> modifyUser(@RequestBody UserBo bo) {
        ssoService.updateUser(bo);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserBo> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        UserBo userBo = ssoService.login(dto.getPassport(), dto.getPassword(), response);
        return new ResponseEntity<>(userBo, HttpStatus.OK);
    }

    @PutMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
        ssoService.logout(CookieUtil.getToken(request));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
