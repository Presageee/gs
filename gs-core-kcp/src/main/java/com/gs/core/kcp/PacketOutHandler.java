package com.gs.core.kcp;

import io.netty.buffer.ByteBuf;

/**
 * Created by linjuntan on 2018/1/28.
 * email: ljt1343@gmail.com
 */
public abstract class PacketOutHandler {
    /**
     * 编码
     * @param control 控制位
     * @param serviceId 业务指令Id
     * @param payload 冗余位
     * @param packet 数据包
     * @return 待发送包
     */
    public abstract ByteBuf encode(int control, int serviceId, int payload, AbstractPacket packet);

    /**
     * 编码
     * @param control 控制位
     * @param serviceId 业务指令Id
     * @param packet 数据包
     * @return 待发送包
     */
    public abstract ByteBuf encode(int control, int serviceId, AbstractPacket packet);

}
