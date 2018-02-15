package com.gs.sso.controller;

import com.gs.sso.controller.bo.UserBo;
import com.gs.sso.controller.dto.LoginDto;
import com.gs.sso.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * author: linjuntan
 * date: 2018/2/6
 */
@RestController
public class SsoController {

    @Autowired
    private SsoService ssoService;


    @PostMapping("/user")
    public Mono<ResponseEntity<Void>> createUser(@RequestBody UserBo bo) {
        ssoService.createUser(bo);
        return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
    }

    @PutMapping("/user")
    public Mono<ResponseEntity<Void>> modifyUser(@RequestBody UserBo bo) {
        ssoService.updateUser(bo);
        return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
    }

    @PostMapping(name = "/login")
    public Mono<ResponseEntity<UserBo>> login(@RequestBody LoginDto dto) {
        UserBo userBo = ssoService.validateLogin(dto.getPassport(), dto.getPassword());
        if (userBo == null) {

        }

        return Mono.just(new ResponseEntity<>(userBo, HttpStatus.OK));

    }

    @PutMapping("/logout")
    public Mono<ResponseEntity<Void>> logout() {
        return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
    }
}
