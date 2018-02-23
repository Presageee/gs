package com.gs.demo.controller;

import com.gs.demo.dao.HelloMapper;
import com.gs.demo.entity.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: linjuntan
 * date: 2018/2/22
 */
@RestController
public class HelloController {

//    @Autowired
    private HelloMapper helloMapper;

    @GetMapping("/demo")
    public String hello() {
        return "hellowrold";
    }

//    @GetMapping(value = "/demo2")
//    public String>hello2() {
//        return Mono.just(JSON.toJSONString(helloMapper.getById(1)));
//    }
//
//    @GetMapping(value = "/demo3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public Hello hello3() {
//        return helloMapper.getById(1);
//    }
}
