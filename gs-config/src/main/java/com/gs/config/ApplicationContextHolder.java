package com.gs.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by linjuntan on 2017/10/22.
 * email: ljt1343@gmail.com
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private  ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public  ApplicationContext getContext() {
        return context;
    }
}
