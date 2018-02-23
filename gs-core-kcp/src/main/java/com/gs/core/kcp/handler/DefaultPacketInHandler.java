package com.gs.core.kcp.handler;

import com.gs.core.kcp.DataPacket;
import com.gs.core.kcp.PacketInHandler;
import com.gs.core.kcp.protocol.CommonProtocol;
import com.gs.core.kcp.protocol.DecodePacketException;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.beykery.jkcp.KcpOnUdp;

/**
 * Created by linjuntan on 2018/1/28.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class DefaultPacketInHandler extends PacketInHandler {
    @Override
    public DataPacket decode(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) throws DecodePacketException {
        if (byteBuf.hasArray()) {
            return null;
        }

        int size = byteBuf.readableBytes();
        byte[] data = new byte[size];

        byteBuf.getBytes(0, data);

        CommonProtocol protocol = new CommonProtocol();
        try {
            protocol.decode(data);
            protocol.getHeader().validate();
        } catch (DecodePacketException e) {
            log.error(" >>> decode packet error,", e);
            throw new DecodePacketException(e);
        }

        return new DataPacket(protocol, kcpOnUdp);
    }
}
