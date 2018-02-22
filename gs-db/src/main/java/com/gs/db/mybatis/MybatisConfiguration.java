package com.gs.db.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by linjuntan on 2017/10/29.
 * email: ljt1343@gmail.com
 */
@Configuration
public class MybatisConfiguration {
//    @Autowired
//    private PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer;

    @Value("${db.name:test}")
    private String dbName;

    @Value("${db.url:jdbc:mysql://127.0.0.1:3306/test}")
    private String dbUrl;

    @Value("${db.username:root}")
    private String username;

    @Value("${db.password:root}")
    private String password;

    @Value("${db.driverName:com.mysql.jdbc.Driver}")
    private String driverName;

    @Value("${db.filters:stat}")
    private String filters;

    @Value("${db.minActive:100}")
    private int maxActive;

    @Value("${db.initialSize:1}")
    private int initialSize;

    @Value("${db.minWait:60000}")
    private int maxWait;

    @Value("${db.minIdle:1}")
    private int minIdle;

    @Value("${db.timeBetweenEvictionRunsMillis:60000}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${db.minEvictableIdleTimeMillis:300000}")
    private int minEvictableIdleTimeMillis;

    @Value("${db.validationQuery:select 'x'}")
    private String validationQuery;

    @Value("${db.testWhileIdle:true}")
    private boolean testWhileIdle;

    @Value("${db.testOnBorrow:false}")
    private boolean testOnBorrow;

    @Value("${db.testOnReturn:false}")
    private boolean testOnReturn;

    @Value("${db.poolPreparedStatements:true}")
    private boolean poolPreparedStatements;

    @Value("${db.maxOpenPreparedStatements:20}")
    private int maxOpenPreparedStatements;

    //,分割
    @Value("${mapper.package:com.gs.sso.dao}")
    private String mapper;

    @Value("${mapper.entity:com.gs.sso.entity}")
    private String entity;

    @Bean("dbMaster")
    public DataSource druidDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setName(dbName);
        druidDataSource.setUrl(dbUrl);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName(driverName);
        druidDataSource.setFilters(filters);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);

        return druidDataSource;
    }



    @Bean("sqlSessionFactory")
    @DependsOn("dbMaster")
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(entity);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @DependsOn("sqlSessionFactory")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Bean("mapperScanner")
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setBasePackage(mapper);
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//
//        return mapperScannerConfigurer;
//    }

}
