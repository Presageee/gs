package com.gs.core.web.mvc;

import com.gs.config.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.ServletException;
import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * author: linjuntan
 * date: 2018/2/22
 */
@Slf4j
public class TomcatServer {
    @Value("${server.contextPath:/}")
    private String contextPath = "/";

    @Value("${server.baseDir:}")
    private String baseDir = "";

    @Value("${server.port:8080}")
    private int port = 8080;

    @Value("${server.bind.address:localhost}")
    private String address;

    public static String scanPack;

    public Tomcat tomcat() throws ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir("".equals(baseDir) ? System.getProperty("java.io.tmpdir") : baseDir);
        tomcat.addWebapp(contextPath, new File(System.getProperty("java.io.tmpdir")).getAbsolutePath());
        return tomcat;
    }

    public void start(String scanPack) throws ServletException {
        Tomcat tomcat = tomcat();
        TomcatServer.scanPack = scanPack;

        log.info(" >>> start up tomcat server");
        CompletableFuture.runAsync(() -> {
            try {
                tomcat.start();
                tomcat.getServer().await();
            } catch (LifecycleException e) {
                log.error(" >>> start up tomcat error", e);
            }
        });
    }
}
