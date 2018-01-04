package com.gs.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by linjuntan on 2017/10/20.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class BaseHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        log.info("baseHandler => " + msg.content());
    }
}
