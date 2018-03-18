package com.gs.demo.filter;

import com.gs.core.web.mvc.filter.GsFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * author: linjuntan
 * date: 2018/2/23
 */
@Slf4j
@Component
public class HelloFilter implements GsFilter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info(" >>> in hello aspect");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    @Override
    public String[] getPathPatterns() {
        return new String[]{"/*"};
    }
}
