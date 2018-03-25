package com.gs.inner.sso.controller;

import com.gs.inner.sso.controller.bo.InnerUserBo;
import com.gs.inner.sso.controller.dto.InnerLoginDto;
import com.gs.inner.sso.service.InnerSsoService;
import com.gs.inner.sso.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * author: theonelee
 * date: 2018/3/1
 */
@RestController
public class InnerSsoController {

    @Autowired
    InnerSsoService innerSsoService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/inner/login")
    public ResponseEntity<InnerUserBo> login(@RequestBody InnerLoginDto dto, HttpServletResponse response) {
        System.out.println("-------->call in inner user login");
        InnerUserBo userBo = innerSsoService.login(dto.getPassport(), dto.getPassword(), response);
        return new ResponseEntity<>(userBo, HttpStatus.OK);
    }

    @PutMapping(value = "/inner/logout")
    public ResponseEntity<Void> logout() {
        innerSsoService.logout(CookieUtil.getToken(request));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    
}
