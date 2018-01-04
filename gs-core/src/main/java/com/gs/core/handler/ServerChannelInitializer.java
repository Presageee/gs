package com.gs.core.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by linjuntan on 2017/10/17.
 * email: ljt1343@gmail.com
 */
public class ServerChannelInitializer extends ChannelInitializer<NioDatagramChannel> {
    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
        ch.pipeline().addLast(new BaseHandler());
        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
    }
}
