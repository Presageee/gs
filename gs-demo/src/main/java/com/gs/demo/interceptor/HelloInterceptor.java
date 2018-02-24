package com.gs.demo.interceptor;

import com.gs.core.web.mvc.interceptor.GsInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author: linjuntan
 * date: 2018/2/24
 */
@Slf4j
@Component
public class HelloInterceptor extends GsInterceptor {
    @Override
    public Integer getOrder() {
        return 0;
    }

    @Override
    public String[] getPathPatterns() {
        return new String[] {"/*"};
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(" >>> in hello interceptor ");
        return true;
    }
}
