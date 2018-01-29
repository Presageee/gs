package com.gs.core.kcp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by linjuntan on 2018/1/8.
 * email: ljt1343@gmail.com
 */
@Slf4j
@Configuration
public class KcpConfiguration {
    /**
     *
     */
    @Value("${kcp.minRto:10}")
    private int minRto;

    /**
     *
     */
    @Value("${kcp.timeout:60000}")
    private int timeout;

    /**
     *
     */
    @Value("${kcp.mtu: 512}")
    private int mtu;

    /**
     *
     */
    @Value("${kcp.sndWnd: 64}")
    private int sndWnd;

    /**
     *
     */
    @Value("${kcp.rcvWnd: 64}")
    private int rcvWnd;

    /**
     *
     */
    @Value("${kcp.noDelay: 1}")
    private int noDelay;

    /**
     *
     */
    @Value("${kcp.interval: 10}")
    private int interval;

    /**
     *
     */
    @Value("${kcp.resend: 2}")
    private int resend;

    /**
     *
     */
    @Value("${kcp.nc: 1}")
    private int nc;

    /**
     *
     */
    @Value("${kcp.workSize: 4}")
    private int workSize;

    /**
     *
     */
    @Value("${kcp.port: 22000}")
    private int port;


    public GsServer gsServer() {
        return null;
    }
}
