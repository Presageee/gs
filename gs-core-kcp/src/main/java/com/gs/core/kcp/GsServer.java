package com.gs.core.kcp;

import com.gs.core.kcp.filterchain.InputFilterChain;
import com.gs.core.kcp.filterchain.OutputFilterChain;
import com.gs.core.kcp.protocol.DecodePacketException;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.beykery.jkcp.KcpOnUdp;
import org.beykery.jkcp.KcpServer;


/**
 * Created by linjuntan on 2018/1/8.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class GsServer extends KcpServer {

    @Getter
    @Setter
    private PacketInHandler inHandler;

    @Getter
    @Setter
    private PacketOutHandler outHandler;

    @Getter
    @Setter
    private InputFilterChain inputFilterChain;

    @Getter
    @Setter
    private OutputFilterChain outputFilterChain;


    public GsServer(int port, int workerSize) {
        super(port, workerSize);

    }

    @Override
    public void handleReceive(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) {
        //1. doInputFilter
        if (inputFilterChain != null && !inputFilterChain.getInFilters().isEmpty()) {
            inputFilterChain.doFilter(byteBuf, kcpOnUdp);
        }

        //2. decode packet
        DataPacket packet;
        try {
            packet = inHandler.decode(byteBuf, kcpOnUdp);
        } catch (DecodePacketException e) {
            log.error(" >>> packet decode error,", e);
            return;
        }

        if (packet == null) {
            return;
        }

        //3. serviceListener handler
        ServiceListener listener = ServiceHandler.getServiceListener(packet.getServiceId());
        if (listener == null) {
            log.error(" >>> no such serviceListener");
            return;
        }

        listener.handler(packet);
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

    /**
     * 发送数据
     * @param control 控制位
     * @param serviceId 业务指令Id
     * @param paylaod 冗余位
     * @param packet 待发送包
     */
    public void send(int control, int serviceId, int payload, AbstractPacket packet, KcpOnUdp kcpOnUdp) {
        //1. create ByteBuf
        ByteBuf buffer = outHandler.encode(control, serviceId, payload, packet);

        //2. do OutputFilter
        if (outputFilterChain != null && outputFilterChain.getOutFilters().isEmpty()) {
            outputFilterChain.doFilter(buffer, null);
        }

        //3, send data
        send(buffer, kcpOnUdp);
        log.debug(" >>> send data on {}", kcpOnUdp);
    }

    /**
     * 发送数据
     * @param control 控制位
     * @param serviceId 业务指令Id
     * @param packet 待发送包
     */
    public void send(int control, int serviceId, AbstractPacket packet, KcpOnUdp kcpOnUdp) {
        send(control, serviceId, 0, packet, kcpOnUdp);
    }

//    public static void main(String[] args) {
//        GsServer gs = new GsServer(6666, 4);
//        gs.noDelay(1, 10, 2, 1);
//        gs.setMinRto(10);
//        gs.wndSize(64, 64);
//        gs.setTimeout(10 * 1000);
//        gs.setMtu(512);
//        gs.start();
//    }
}
