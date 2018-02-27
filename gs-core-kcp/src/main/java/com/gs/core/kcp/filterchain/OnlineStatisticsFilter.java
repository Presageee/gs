package com.gs.core.kcp.filterchain;

import com.gs.core.kcp.GsContext;
import com.gs.core.kcp.OnlineCounter;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.beykery.jkcp.KcpOnUdp;

/**
 * author: linjuntan
 * date: 2018/2/27
 */
@Slf4j
public class OnlineStatisticsFilter implements Filter {

    @Getter
    @Setter
    private OnlineCounter onlineCounter;

    @Override
    public void doFilter(ByteBuf buf, KcpOnUdp kcpOnUdp) {
        if (GsContext.addCountSetElement(kcpOnUdp.getRemote())) {
            onlineCounter.asyncUpdateOnlineCount();
        }
    }
}
