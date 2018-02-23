package com.gs.core.kcp.security;

import com.gs.common.utils.AESUtil;

/**
 * Created by linjuntan on 2018/1/28.
 * email: ljt1343@gmail.com
 */
public class SecurityHandler {

    public static byte[] decrypt(byte[] data) {
        return AESUtil.decrypt(data);
    }

    public static byte[] encrypt(byte[] data) {
        return AESUtil.encrypt(data);
    }
}
