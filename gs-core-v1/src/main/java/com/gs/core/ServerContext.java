package com.gs.core;

import com.gs.common.utils.AESUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by linjuntan on 2017/10/15.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class ServerContext {
    private ServerContext() {

    }

    public static ServerContext getInstance() {
        return ServerContextHolder.context;
    }

    private static class ServerContextHolder {
        static ServerContext context = new ServerContext();
    }

    private Map<String, Long> heartbeatMap = new ConcurrentHashMap<>();

    private volatile long onlineCnt;

    private Channel channel;

    private int packetLength;

    private boolean isEncrypt = true;

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }

    public long getOnlineCnt() {
        return onlineCnt;
    }

    public void setOnlineCnt(long onlineCnt) {
        this.onlineCnt = onlineCnt;
    }

    private LinkedBlockingQueue<DatagramPacket> heartbeatQueue = new LinkedBlockingQueue<>();

    public void putHeartbeat(DatagramPacket packet) throws InterruptedException {
        heartbeatQueue.put(packet);
    }

    public DatagramPacket takeHeartbeat() throws InterruptedException {
        return heartbeatQueue.take();
    }

    public Map<String, Long> getHeartbeatMap() {
        return heartbeatMap;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void sendUdpMessage(byte[] data, InetSocketAddress sender) throws PacketLengthExceedException {
        if (data.length > packetLength) {
            throw new PacketLengthExceedException();
        }
        if (isEncrypt) {
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(data), sender));
            return;
        }

        byte[] encryptData = AESUtil.encrypt(data);
        if (encryptData == null) return;

        channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(encryptData), sender));
    }

//    public void sendUdpMessage(CommonPacket packet) throws PacketLengthExceedException {
//        if (packet.getData().length > packetLength) {
//            throw new PacketLengthExceedException();
//        }
//
//        if (isEncrypt) {
//            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(packet.getData()), packet.getSender()));
//            return;
//        }
//
//        byte[] encryptData = AESUtil.encrypt(packet.getData());
//        if (encryptData == null) return;
//
//        channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(encryptData), packet.getSender()));
//    }
}
