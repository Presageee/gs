package com.gs.sso.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


/**
 * author: linjuntan
 * date: 2018/2/22
 */
@Configuration
@MapperScan(basePackages = "com.gs.sso.dao")
public class SsoConfiguration {

}
