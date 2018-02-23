package com.gs.core.kcp.startup;

import com.gs.core.kcp.GsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * author: linjuntan
 * date: 2018/2/23
 */
@Component
public class KcpStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GsServer server;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        server.start();
    }
}
