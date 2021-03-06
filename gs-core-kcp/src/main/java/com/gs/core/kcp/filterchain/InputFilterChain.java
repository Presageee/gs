package com.gs.core.kcp.filterchain;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.beykery.jkcp.KcpOnUdp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
public class InputFilterChain {
    @Getter
    @Setter
    private List<Filter> inFilters;

    public InputFilterChain() {
        inFilters = new ArrayList<>(2);
    }

    public void doFilter(ByteBuf buf, KcpOnUdp kcpOnUdp) {
        inFilters.forEach(e -> e.doFilter(buf, kcpOnUdp));
    }
}
