package com.gs.core.kcp;

import io.netty.buffer.ByteBuf;
import org.beykery.jkcp.KcpOnUdp;

/**
 * Created by linjuntan on 2018/1/28.
 * email: ljt1343@gmail.com
 */
public abstract class PacketInHandler {

    public abstract byte[] decode(ByteBuf byteBuf, KcpOnUdp kcpOnUdp);

}
