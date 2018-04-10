package com.gs.inner.sso.controller;


import com.gs.common.utils.CommonUtil;
import com.gs.inner.sso.controller.bo.InnerUserBo;
import com.gs.inner.sso.controller.dto.InnerLoginDto;
import com.gs.inner.sso.service.InnerSsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author: theonelee
 * date: 2018/3/23
 */
@Controller
public class InnerSsoPageController {

    @Autowired
    InnerSsoService innerSsoService;

    @Autowired
    private HttpServletRequest request;

    @Value("#{'${server.inner.index.uri:/inner/index}'}")
    private String indexUrl;

    @GetMapping(value = "/inner/loginPage")
    public String loginPage(){
        return "/innerLogin";
    }

    @PostMapping(value = "/inner/loginPage/post")
    public String loginPagePost(String passport,String password, HttpServletResponse response){
        InnerUserBo userBo = innerSsoService.login(passport, password, response);
        if (!CommonUtil.isNull(userBo)){
            return "redirect:"+indexUrl;//todo here
        }
        return "redirect:/inner/loginPage?error";
    }

    @GetMapping(value = "${server.inner.index.uri}/")//这里写一个控制器方法，用于给here标注的来跳转

}
