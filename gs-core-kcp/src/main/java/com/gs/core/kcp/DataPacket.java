package com.gs.core.kcp;

import com.gs.core.kcp.protocol.CommonProtocol;
import org.beykery.jkcp.KcpOnUdp;

/**
 * Created by linjuntan on 2018/1/28.
 * email: ljt1343@gmail.com
 */
public class DataPacket {
    private CommonProtocol  protocol;

    private KcpOnUdp kcpOnUdp;

    public DataPacket() {
    }

    public DataPacket(CommonProtocol protocol, KcpOnUdp kcpOnUdp) {
        this.protocol = protocol;
        this.kcpOnUdp = kcpOnUdp;
    }

    public CommonProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(CommonProtocol protocol) {
        this.protocol = protocol;
    }

    public KcpOnUdp getKcpOnUdp() {
        return kcpOnUdp;
    }

    public void setKcpOnUdp(KcpOnUdp kcpOnUdp) {
        this.kcpOnUdp = kcpOnUdp;
    }
}
