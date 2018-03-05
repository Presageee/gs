package com.gs.core.web.mvc;

import com.gs.core.web.mvc.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * author: linjuntan
 * date: 2018/2/22
 */
@Slf4j
public class TomcatServer {
    /**
     * SystemId
     */
    private String systemId;

    /**
     * context path
     */
    private String contextPath = "/";

    /**
     * base Dir
     */
    private String baseDir = "";

    /**
     * port, default 8080
     */
    private int port = 8080;

    /**
     * spring scan package path
     */
    public static String scanPack;

    /**
     * custom filters, order by insert
     */
    public static List<String> customFilters;

    /**
     * open token aspect
     */
    public static boolean openTokenFilter = false;

    /**
     * aspect uri
     */
    public static List<String> filterUris;

    /**
     * max upload file size,default 2m
     */
    public static int maxFileSize = 2097152;

    /**
     * max single request upload file size, default 2m
     */
    public static int maxRequestFileSize = 2097152;

    /**
     * open cors aspect
     */
    public static boolean openCorsFilter;

    public TomcatServer() {
        init();
    }

    public Tomcat tomcat() throws ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        tomcat.setBaseDir(baseDir);
        tomcat.addWebapp(contextPath, new File(System.getProperty("java.io.tmpdir")).getAbsolutePath());
        return tomcat;
    }

    public void start() throws ServletException {
        Tomcat tomcat = tomcat();

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

    private void init() {
        ServerConfig config = new ServerConfig();

        port = config.getConfigIntProperty("server.port", 8080);
        baseDir = config.getConfigProperty("server.base.dir", "");
        contextPath = config.getConfigProperty("server.context.path", "/");
        scanPack = config.getConfigProperty("spring.scan.path", "com.gs");
        maxFileSize = config.getConfigIntProperty("server.max.file.size", 2097152);
        maxRequestFileSize = config.getConfigIntProperty("server.max.request.file.size", 2097152);
        openCorsFilter = Boolean.valueOf(config.getConfigProperty("server.open.cors", "false"));

        String filterStr = config.getConfigProperty("server.custom.filters", "");
        if (!"".equals(filterStr)) {
            customFilters = Arrays.asList(filterStr.split(","));
        } else {
            customFilters = null;
        }

        String uris = config.getConfigProperty("server.aspect.uris", "login,logout,user");
        filterUris = Arrays.asList(uris.split(",")).stream().map(e -> contextPath + e).collect(Collectors.toList());

        systemId = config.getConfigProperty("systemId");
    }
}
