package com.gs.core.kcp;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
public class GsContext {
    private static GsServer instance;

    private static final CopyOnWriteArraySet<InetSocketAddress> onlineCountSet = new CopyOnWriteArraySet<>();

    public static void setGsServer(GsServer gsServer) {
        instance = gsServer;
    }

    public static GsServer server() {
        return instance;
    }

    public static boolean addCountSetElement(InetSocketAddress socketAddress) {
        return onlineCountSet.add(socketAddress);
    }

    public static void removeCountSetElement(InetSocketAddress socketAddress) {
        onlineCountSet.remove(socketAddress);
    }

    public static int getOnlineCount() {
        return onlineCountSet.size();
    }
}
