package com.gs.core.kcp;

import com.alibaba.fastjson.JSON;
import com.gs.cache.CacheProxy;
import com.sun.management.OperatingSystemMXBean;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.util.concurrent.CompletableFuture;

import static com.gs.core.kcp.constant.KcpConstants.ONLINE_COUNT_KEY;

/**
 * author: linjuntan
 * date: 2018/2/27
 */
@Slf4j
public class OnlineCounter {
    @Setter
    @Getter
    private CacheProxy cacheProxy;

    private OperatingSystemMXBean os;

    private String serverName;

    public OnlineCounter(CacheProxy cacheProxy, String serverName) {
        this.cacheProxy = cacheProxy;
        this.serverName = serverName;


        os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    }

    /**
     * 异步更新在线用户数
     */
    public void asyncUpdateOnlineCount() {
        CompletableFuture.runAsync(() -> {
            Info info = new Info(GsContext.getOnlineCount(),
                    os.getProcessCpuLoad(), os.getSystemCpuLoad());

            String key = String.format(ONLINE_COUNT_KEY, serverName);
            cacheProxy.set(key, JSON.toJSONString(info));
            log.info(" >>> online info => {}", info.toString());
        });
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class Info {
        private int onlineCount;
        private double cpuLoad;
        private double systemLoad;
    }
}
