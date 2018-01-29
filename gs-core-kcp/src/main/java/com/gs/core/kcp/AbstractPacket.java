package com.gs.core.kcp;

import com.gs.core.kcp.exception.NoSuchMethodException;

import java.io.Serializable;

/**
 * Created by linjuntan on 2018/1/29.
 * email: ljt1343@gmail.com
 */
public interface AbstractPacket extends Serializable {
    default byte[] toByte() throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }
}
