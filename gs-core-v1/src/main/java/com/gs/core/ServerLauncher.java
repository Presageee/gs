package com.gs.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.concurrent.CompletableFuture;

/**
 * Created by linjuntan on 2017/10/15.
 * email: ljt1343@gmail.com
 */
public class ServerLauncher {
    private volatile boolean isFirst = false;

    private ServerLauncher() {

    }

    public static ServerLauncher getInstance() {
        return ServerLauncherHolder.serverLauncher;
    }

    private static class ServerLauncherHolder {
        private static ServerLauncher serverLauncher = new ServerLauncher();
    }

    public void init(int port, ChannelHandler initializer) throws Exception {
        if (!isFirst) {

            synchronized (this) {
//                Deprecated
//                //read config
//                String configFile = String.format(Constants.CONFIG_FILE_NAME,
//                        System.getProperty(Constants.CONFIG_FILE_KEY));
//                ResourceReader.getInstance().loadByName(configFile);

                if (!isFirst) {
                    CompletableFuture.runAsync(() -> {
                        EventLoopGroup group = new NioEventLoopGroup();
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(group)
                                .channel(NioDatagramChannel.class)
                                .localAddress(port)
                                .handler(initializer);
                        try {
                            Channel channel = bootstrap.bind(port).sync().channel();
                            ServerContext.getInstance().setChannel(channel);
                            channel.closeFuture().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

                    isFirst = true;
                }
            }
        }
    }


}
