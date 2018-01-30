package com.gs.core.kcp;

import com.gs.common.util.ReflectUtils;
import com.gs.core.kcp.filterchain.Filter;
import com.gs.core.kcp.filterchain.InputFilterChain;
import com.gs.core.kcp.filterchain.OutputFilterChain;
import com.gs.core.kcp.handler.DefaultPackOutHandler;
import com.gs.core.kcp.handler.DefaultPacketInHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

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
    private int minRto = 10;

    /**
     *
     */
    @Value("${kcp.timeout:60000}")
    private int timeout = 60000;

    /**
     *
     */
    @Value("${kcp.mtu: 512}")
    private int mtu = 512;

    /**
     *
     */
    @Value("${kcp.sndWnd: 64}")
    private int sndWnd = 64;

    /**
     *
     */
    @Value("${kcp.rcvWnd: 64}")
    private int rcvWnd = 64;

    /**
     *
     */
    @Value("${kcp.noDelay: 1}")
    private int noDelay = 1;

    /**
     *
     */
    @Value("${kcp.interval: 10}")
    private int interval = 10;

    /**
     *
     */
    @Value("${kcp.resend: 2}")
    private int resend = 2;

    /**
     *
     */
    @Value("${kcp.nc: 1}")
    private int nc = 1;

    /**
     *
     */
    @Value("${kcp.workSize: 4}")
    private int workSize = 4;

    /**
     *
     */
    @Value("${kcp.port: 22000}")
    private int port = 22000;


    @Value("${kcp.default.handler: true}")
    private boolean defaultHandler = true;

    @Value("${kcp.customer.in.handler:}")
    private String inputHandler = "";

    @Value("${kcp.customer.out.handler:}")
    private String outputHandler = "";

    @Value("${kcp.in.filters:}")
    private String inputFilter = "";

    @Value("${kcp.out.filters:}")
    private String outputFilter = "";

    @Bean
    public GsServer gsServer() {
        GsServer gs = new GsServer(port, workSize);
        //base config
        gs.noDelay(noDelay, interval, resend, nc);
        gs.setMinRto(minRto);
        gs.wndSize(sndWnd, rcvWnd);
        gs.setTimeout(timeout);
        gs.setMtu(mtu);

        //input
        gs.setInHandler(createInHandler());
        gs.setInputFilterChain(createInputFilterChain());

        //output
        gs.setOutHandler(createOutHandler());
        gs.setOutputFilterChain(createOutputFilterChain());

        GsContext.setGsServer(gs);

        return gs;
    }

    private PacketInHandler createInHandler() {
        if (defaultHandler) {
            return new DefaultPacketInHandler();
        }

        if ("".equals(inputHandler)) {
            log.warn(" >>> default handler flag is false, inputHandler is null, create defaultInHandler");
            return new DefaultPacketInHandler();
        }

        return (PacketInHandler) ReflectUtils.createObjectByClassName(inputHandler);
    }

    private PacketOutHandler createOutHandler() {
        if (defaultHandler) {
            return new DefaultPackOutHandler();
        }

        if ("".equals(outputHandler)) {
            log.warn(" >>> default handler flag is false, outputHandler is null, create defaultOutHandler");
            return new DefaultPackOutHandler();
        }

        return (PacketOutHandler) ReflectUtils.createObjectByClassName(outputHandler);
    }

    private InputFilterChain createInputFilterChain() {
        InputFilterChain ifc = new InputFilterChain();
        if (StringUtils.isEmpty(inputFilter)) {
            return ifc;
        }

        String []ifs = inputFilter.split(",");
        for (String filter : ifs) {
            if (StringUtils.isEmpty(filter)) {
                continue;
            }

            ifc.getInFilters()
                    .add((Filter) ReflectUtils.createObjectByClassName(filter));
        }

        return ifc;
    }

    private OutputFilterChain createOutputFilterChain() {
        OutputFilterChain ofc = new OutputFilterChain();
        if (StringUtils.isEmpty(outputFilter)) {
            return ofc;
        }

        String []ofs = outputFilter.split(",");
        for (String filter : ofs) {
            if (StringUtils.isEmpty(filter)) {
                continue;
            }

            ofc.getOutFilters()
                    .add((Filter) ReflectUtils.createObjectByClassName(filter));
        }

        return ofc;
    }


}
