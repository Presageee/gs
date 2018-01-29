package com.gs.core.kcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.beykery.jkcp.KcpClient;
import org.beykery.jkcp.KcpOnUdp;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

/**
 * Created by linjuntan on 2018/1/8.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class GsClient extends KcpClient {
    @Override
    public void handleReceive(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) {
        String content = byteBuf.toString(Charset.forName("utf-8"));
        System.out.println("conv:" + kcpOnUdp.getKcp().getConv() + " recv:" + content + " kcp-->" + kcpOnUdp);
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(2048);
        buf.writeBytes(content.getBytes(Charset.forName("utf-8")));
        kcpOnUdp.send(buf);
        byteBuf.release();
    }

    @Override
    public void handleException(Throwable throwable, KcpOnUdp kcpOnUdp) {

    }

    @Test
    public void test1() throws InterruptedException {
//        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        Object obj = new Object();
        CompletableFuture.runAsync(() -> {
            GsClient tc = new GsClient();
            tc.noDelay(1, 20, 2, 1);
            tc.setMinRto(10);
            tc.wndSize(32, 32);
            tc.setTimeout(10 * 1000);
            tc.setMtu(512);
            // tc.setConv(121106);//默认conv随机

            tc.connect(new InetSocketAddress("127.0.0.1", 8888));
            tc.start();
            String content = "sdfkasd你好。。。。。。。";
            ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(1500);
            bb.writeBytes(content.getBytes(Charset.forName("utf-8")));
            tc.send(bb);
        });
        synchronized (obj) {
            obj.wait();
        }
    }

    public static void main(String[] args) {
        GsClient tc = new GsClient();
        tc.noDelay(1, 20, 2, 1);
        tc.setMinRto(10);
        tc.wndSize(32, 32);
        tc.setTimeout(10 * 1000);
        tc.setMtu(512);
        // tc.setConv(121106);//默认conv随机

        tc.connect(new InetSocketAddress("localhost", 2222));
        tc.start();
        String content = "sdfkasd你好。。。。。。。";
        ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(1500);
        bb.writeBytes(content.getBytes(Charset.forName("utf-8")));
        tc.send(bb);
    }
}
