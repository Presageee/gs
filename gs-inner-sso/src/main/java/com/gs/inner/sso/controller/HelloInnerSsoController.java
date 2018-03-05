package com.gs.inner.sso.controller;

import com.gs.inner.sso.entity.InnerUser;
import com.gs.inner.sso.entity.Role;
import com.gs.inner.sso.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * author: theonelee
 * date: 2018/2/28
 */
@RestController
public class HelloInnerSsoController {
    @Autowired
    private HelloService helloService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/innerUser")
    public ResponseEntity<Void> createUser(@RequestBody InnerUser innerUser) {
        helloService.createUser(innerUser);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(value = "/innerUser")
    public ResponseEntity<Void> modifyUser(@RequestBody InnerUser innerUser) {
        helloService.updateUser(innerUser);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/innerUser/{id}")
    public ResponseEntity<InnerUser> getUserById(@PathVariable Integer id){
        InnerUser innerUser =helloService.getRoleListByUserId(id);
        return new ResponseEntity<InnerUser>(innerUser,HttpStatus.OK);
    }


    @PostMapping(value = "/innerRole")
    public ResponseEntity<Void> createUser(@RequestBody Role role) {
        helloService.createRole(role);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/innerRole")
    public ResponseEntity<Role> getRoleById(Integer id){
        Role role=helloService.getRoleById(id);
        return new ResponseEntity<Role>(role,HttpStatus.OK);
    }
}
