package com.gs.common.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by linjuntan on 2017/10/29.
 * email: ljt1343@gmail.com
 */
public class MD5Utils {
    private final String salt = "server!@#";

    public String encode(String code) {
        if (code != null && !code.equals("")) {
            return DigestUtils.md5Hex(DigestUtils.md5Hex(code) + salt);
        }
        return null;
    }

}
