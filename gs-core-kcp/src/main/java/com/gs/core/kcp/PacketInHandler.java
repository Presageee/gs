package com.gs.core.kcp;

import com.gs.core.kcp.protocol.DecodePacketException;
import io.netty.buffer.ByteBuf;
import org.beykery.jkcp.KcpOnUdp;

/**
 * Created by linjuntan on 2018/1/28.
 * email: ljt1343@gmail.com
 */
public abstract class PacketInHandler {

    /**
     * 数据包解码
     * @param byteBuf 接收到的数据
     * @param kcpOnUdp kcp通道
     * @return 解码后的数据
     * @throws DecodePacketException 解码出错
     */
    public abstract DataPacket decode(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) throws DecodePacketException;

}
