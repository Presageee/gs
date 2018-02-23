package com.gs.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by linjuntan on 2018/1/30.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class ReflectUtil {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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

    /**
     * convert object by same name field
     * @param src src obj
     * @param dest desc obj
     * @param <S> src class
     * @param <D> dest class
     */
    @SuppressWarnings("unchecked")
    public static <S, D> void convertObject(S src, D dest) {
        Field[] destFields = dest.getClass().getDeclaredFields();
        Arrays.asList(destFields).forEach(df -> {
            try {
                Field sf = src.getClass().getDeclaredField(df.getName());

                sf.setAccessible(true);
                df.setAccessible(true);
                if (df.isAnnotationPresent(com.gs.common.annotation.Date.class)) {
                    LocalDateTime date = LocalDateTime.ofInstant(new Date((Long)sf.get(src)).toInstant(), ZoneId.systemDefault());
                    df.set(dest, date.format(dtf));
                } else if (df.isAnnotationPresent(com.gs.common.annotation.Timestamp.class)) {
                    LocalDateTime date = LocalDateTime.parse((CharSequence) sf.get(src), dtf);
                    df.set(dest, date.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                } else {
                    df.set(dest, sf.get(src));
                }
            } catch (Exception e1) {
                log.warn(" >>> {} without field {}", src, df.getName(), e1);
            }
        });
    }
}
