package com.gs.core.kcp.filterchain;

import io.netty.buffer.ByteBuf;
import org.beykery.jkcp.KcpOnUdp;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
public interface Filter {
    void doFilter(ByteBuf buf, KcpOnUdp kcpOnUdp);
}
