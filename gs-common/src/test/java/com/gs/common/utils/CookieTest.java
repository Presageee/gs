package com.gs.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class CookieTest {
    @Test
    public void toStringTest() throws Exception {
        Cookie cookie = new Cookie();
        cookie.setExpired(1000l);
        cookie.setName("a");
        cookie.setValue("v");
        cookie.setDomain("1");
        cookie.setPath("123");

        log.info(cookie.toString());
    }


}
