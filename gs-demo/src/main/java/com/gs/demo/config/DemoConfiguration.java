package com.gs.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * author: linjuntan
 * date: 2018/2/22
 */
@Configuration
@MapperScan("com.gs.demo.dao")
public class DemoConfiguration {

}
