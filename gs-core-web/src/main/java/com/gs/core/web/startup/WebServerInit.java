package com.gs.core.web.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.stereotype.Component;
import reactor.ipc.netty.http.server.HttpServer;

import java.util.concurrent.CompletableFuture;

/**
 * Created by linjuntan on 2017/10/22.
 * email: ljt1343@gmail.com
 */
@Component
public class WebServerInit implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private HttpServer httpServer;
    @Autowired
    private ReactorHttpHandlerAdapter adapter;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        CompletableFuture.runAsync(() -> {
            httpServer.newHandler(adapter).block();
        });
    }
}
