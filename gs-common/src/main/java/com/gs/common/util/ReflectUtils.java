package com.gs.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class ReflectUtils {

    /**
     * create object by class name
     * @param clazzName class name
     * @return new instance
     */
    public static Object createObjectByClassName(String clazzName) {
        try {
            Class clazz = Class.forName(clazzName);
            return clazz.newInstance();
        } catch (Exception e) {
            log.error(" >>> create {} instance error!", clazzName, e);
        }
        return null;
    }
}
