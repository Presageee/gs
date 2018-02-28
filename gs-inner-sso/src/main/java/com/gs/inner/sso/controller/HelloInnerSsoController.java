package com.gs.inner.sso.controller;

import com.gs.inner.sso.entity.Role;
import com.gs.inner.sso.entity.User;
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
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        helloService.createUser(user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(value = "/innerUser")
    public ResponseEntity<Void> modifyUser(@RequestBody User user) {
        helloService.updateUser(user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/innerUser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id){
        User user=helloService.getUserById(id);
        return new ResponseEntity<User>(user,HttpStatus.OK);
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
