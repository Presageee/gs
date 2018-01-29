package com.gs.core.kcp;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
public class GsContext {
    private static GsServer instance;

    public static void setGsServer(GsServer gsServer) {
        instance = gsServer;
    }

    public static GsServer server() {
        return instance;
    }
}
