package com.gs.core.kcp;

import com.gs.core.kcp.protocol.CommonProtocol;
import lombok.Getter;
import lombok.Setter;
import org.beykery.jkcp.KcpOnUdp;

/**
 * Created by linjuntan on 2018/1/28.
 * email: ljt1343@gmail.com
 */
public class DataPacket {
    @Getter
    @Setter
    private int serviceId;

    @Getter
    @Setter
    private CommonProtocol  protocol;

    @Getter
    @Setter
    private KcpOnUdp kcpOnUdp;

    public DataPacket() {
    }

    public DataPacket(CommonProtocol protocol, KcpOnUdp kcpOnUdp) {
        this.protocol = protocol;
        this.kcpOnUdp = kcpOnUdp;
    }

    public DataPacket(int serviceId, CommonProtocol protocol, KcpOnUdp kcpOnUdp) {
        this.serviceId = serviceId;
        this.protocol = protocol;
        this.kcpOnUdp = kcpOnUdp;
    }
}
