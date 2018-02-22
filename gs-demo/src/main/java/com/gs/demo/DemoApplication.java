package com.gs.demo;

import com.gs.core.web.mvc.TomcatServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;

/**
 * author: linjuntan
 * date: 2018/2/22
 */
public class DemoApplication {
    public static Object obj = new Object();

    public static void main(String[] args) throws ServletException {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.gs");
//        applicationContext.start();
//
//        synchronized (obj) {
//            try {
//                obj.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        TomcatServer tomcatServer = new TomcatServer();
        tomcatServer.start("com.gs");
                synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
