package com.gs.core.web.mvc.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * author: linjuntan
 * date: 2018/2/24
 */
public class GsInterceptor extends HandlerInterceptorAdapter {
    /**
     * 拦截器执行顺序值
     * @return 序号
     */
    public Integer getOrder() {
        return -1;
    }

    /**
     * 拦截器拦截路径
     * @return 路径数组
     */
    public String[] getPathPatterns() {
        return new String[] {"/*"};
    }
}
