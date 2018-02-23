package com.gs.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by linjuntan on 2017/10/29.
 * email: ljt1343@gmail.com
 */
public class MD5Util {
    private static final String salt = "server!@#";

    public static String encode(String code) {
        if (code != null && !code.equals("")) {
            return DigestUtils.md5Hex(DigestUtils.md5Hex(code) + salt);
        }
        return null;
    }

}
