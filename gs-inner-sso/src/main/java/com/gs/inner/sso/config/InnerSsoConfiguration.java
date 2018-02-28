package com.gs.inner.sso.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


/**
 * author: theonelee
 * date: 2018/2/28
 */
@Configuration
@MapperScan(basePackages = "com.gs.inner.sso.dao")
public class InnerSsoConfiguration {

}
