package com.gs.core.kcp;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * Created by linjuntan on 2018/1/8.
 * email: ljt1343@gmail.com
 */
public class GsServerTest {
    @Test
    public void test() {
        Object obj = new Object();
        CompletableFuture.runAsync(() -> {
            GsServer gs = new GsServer(8888, 1);
            gs.noDelay(1, 10, 2, 1);
            gs.setMinRto(10);
            gs.wndSize(64, 64);
            gs.setTimeout(10 * 1000);
            gs.setMtu(512);
            gs.start();
        });
//        synchronized (obj) {
//            try {
//                obj.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public static void main(String[] args) {
        GsServer gs = new GsServer(6666, 1);
        gs.noDelay(1, 10, 2, 1);
        gs.setMinRto(10);
        gs.wndSize(64, 64);
        gs.setTimeout(10 * 1000);
        gs.setMtu(512);
        gs.start();
    }
}
