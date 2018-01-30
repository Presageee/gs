package com.gs.core.kcp;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
public abstract class ServiceListener {
    @Setter
    @Getter
    private int serviceId;

    public ServiceListener(int serviceId) {
        this.serviceId = serviceId;
        register();
    }

    public void register() {
        ServiceHandler.registerListener(serviceId, this);
    }

    public abstract void handler(DataPacket dataPacket);
}
