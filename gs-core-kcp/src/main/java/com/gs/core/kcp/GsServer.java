package com.gs.core.kcp;

import com.gs.common.util.AESUtils;
import com.gs.core.kcp.protocol.CommonProtocol;
import com.gs.core.kcp.protocol.DecodePacketException;
import com.gs.core.kcp.protocol.Header;
import com.gs.core.kcp.protocol.SessionIdGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.beykery.jkcp.KcpOnUdp;
import org.beykery.jkcp.KcpServer;

import java.nio.charset.Charset;

/**
 * Created by linjuntan on 2018/1/8.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class GsServer extends KcpServer {

    public GsServer(int port, int workerSize) {
        super(port, workerSize);

    }

    @Override
    public void handleReceive(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) {
        if (!byteBuf.hasArray()) {
            int size = byteBuf.readableBytes();
            byte[] data = new byte[size];

            byteBuf.getBytes(0, data);

            CommonProtocol protocol = new CommonProtocol();
            try {
                protocol.decode(data);
                protocol.getHeader().validate();
            } catch (DecodePacketException e) {
                log.error(" >>> decode packet error,", e);
            }

            byte[] decryptData = AESUtils.decrypt(protocol.getBody());
            log.info(" >>> content is  {}, kcp is {}", new String(decryptData, Charset.forName("UTF-8")), kcpOnUdp);
        }
        byte[] send = new String("server send").getBytes();
        byte[] encrD = AESUtils.encrypt(send);
        Header header = new Header(SessionIdGenerator.generateSessionId(), 5, 1, 1, encrD.length);
        CommonProtocol protocol = new CommonProtocol(header, encrD);
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(2048);
        buf.writeBytes(protocol.toByte());
        kcpOnUdp.send(buf);
    }

    @Override
    public void handleException(Throwable throwable, KcpOnUdp kcpOnUdp) {
        log.info("kcp exception, ", throwable);
    }

    @Override
    public void handleClose(KcpOnUdp kcpOnUdp) {
        log.info("客户端离开:" + kcpOnUdp);
        log.info("waitSnd:" + kcpOnUdp.getKcp().waitSnd());
    }

    public static void main(String[] args) {
        GsServer gs = new GsServer(6666, 4);
        gs.noDelay(1, 10, 2, 1);
        gs.setMinRto(10);
        gs.wndSize(64, 64);
        gs.setTimeout(10 * 1000);
        gs.setMtu(512);
        gs.start();
    }
}
