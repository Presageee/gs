package com.gs.common.utils;

import java.util.UUID;

/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
public class CommonUtil {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNull(Object ...objects) {

        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBlank(String str) {
        return str != null && "".equals(str);
    }

    public static boolean isBlank(String ...strs) {
        for (String s : strs) {
            if (s != null && "".equals(s)) {
                return true;
            }
        }

        return false;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
