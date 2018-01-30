package com.gs.core.kcp;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class ServiceHandler {
    private static ConcurrentHashMap<Integer, ServiceListener> listenerMap;

    public static void registerListener(int serviceId, ServiceListener listener) {
        listenerMap.putIfAbsent(serviceId, listener);
        log.info(" >>> serviceListener {} was registered", listener.getClass().getName());
    }

    public static ServiceListener getServiceListener(int serviceId) {
        return listenerMap.get(serviceId);
    }
}
