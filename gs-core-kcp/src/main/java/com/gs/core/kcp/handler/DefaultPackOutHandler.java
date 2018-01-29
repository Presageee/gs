package com.gs.core.kcp.handler;

import com.gs.core.kcp.AbstractPacket;
import com.gs.core.kcp.DataPacket;
import com.gs.core.kcp.PacketOutHandler;
import com.gs.core.kcp.exception.NoSuchMethodException;
import com.gs.core.kcp.protocol.CommonProtocol;
import com.gs.core.kcp.protocol.DecodePacketException;
import com.gs.core.kcp.protocol.Header;
import com.gs.core.kcp.protocol.SessionIdGenerator;
import com.gs.core.kcp.security.SecurityHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.beykery.jkcp.KcpOnUdp;

/**
 * Created by linjuntan on 2018/1/29.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class DefaultPackOutHandler extends PacketOutHandler {

    @Override
    public ByteBuf encode(int control, int serviceId, int payload, AbstractPacket packet) {
        //1. encrypt data
        byte[] encryptData;
        try {
            encryptData = SecurityHandler.encrypt(packet.toByte());
        } catch (Exception e) {
            log.error(" >>> encrypt data error,", e);
            return null;
        }

        //2. create header
        Header header = new Header(SessionIdGenerator.generateSessionId(),
                control, serviceId, payload, encryptData.length);

        //3 create protocol
        CommonProtocol protocol = new CommonProtocol(header, encryptData);

        //4. create ByteBuf, need release, release by send data
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(1024);
        buffer.writeBytes(protocol.toByte());

        return buffer;
    }

    @Override
    public ByteBuf encode(int control, int serviceId, AbstractPacket packet) {
        return encode(control, serviceId, 0, packet);
    }
}
