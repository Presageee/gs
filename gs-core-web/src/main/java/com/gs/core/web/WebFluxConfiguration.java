package com.gs.core.web;

import com.gs.config.ApplicationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.ipc.netty.http.server.HttpServer;

/**
 * Created by linjuntan on 2018/2/5.
 * email: ljt1343@gmail.com
 */
@Configuration
@EnableWebFlux
public class WebFluxConfiguration {
    @Value("${server.port:8080}")
    private int port;

    @Value("${server.bind.host:localhost}")
    private String host;

    @Autowired
    private ApplicationContextHolder contextHolder;

    @Bean
    public ReactorHttpHandlerAdapter reactorHttpHandlerAdapter() {
        HttpHandler handler = WebHttpHandlerBuilder.applicationContext(contextHolder.getContext()).build();
        return new ReactorHttpHandlerAdapter(handler);
    }

    @Bean
    public HttpServer httpServer() {
        return HttpServer.create(host, port);
    }
}
