package com.gs.core;

import com.gs.core.handler.ServerChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

/**
 * Created by linjuntan on 2017/10/22.
 * email: ljt1343@gmail.com
 */
@Configuration
public class NettyConfiguration {

    @Value("${server.port:6666}")
    private int port;


    @Bean(name = "serverChannels")
    @Scope("singleton")
    public ServerChannelInitializer serverChannelInitializer() {
        return new ServerChannelInitializer();
    }

    @Bean(name = "nettyServer")
    @Scope("singleton")
    @DependsOn({"serverChannels"})
    public ServerLauncher serverLauncher(@Autowired ServerChannelInitializer initializer) throws Exception {
        ServerLauncher.getInstance().init(port, initializer);
        return ServerLauncher.getInstance();
    }
}
