package com.gs.core.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by linjuntan on 2017/11/29.
 * email: ljt1343@gmail.com
 */
public class SessionIdGenerator {

    private static AtomicInteger sessionIdSeq = new AtomicInteger(0);

    public static Integer generateSessionId() {
        int next = sessionIdSeq.incrementAndGet();

        if (next >= Integer.MAX_VALUE - 10) {
            sessionIdSeq = new AtomicInteger(0);
        }

        return next;
    }


}
