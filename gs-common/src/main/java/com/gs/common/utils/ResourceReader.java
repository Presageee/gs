package com.gs.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by linjuntan on 2017/10/15.
 * email: ljt1343@gmail.com
 */
@Deprecated
public class ResourceReader {

    private static Map<String, String> configKVMap = new HashMap<>();

    private ResourceReader() {
    }

    public static ResourceReader getInstance() {
        return ResourceReaderHolder.resourceReader;
    }

    private String getValue(String key) throws Exception {
        return configKVMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public void loadByName(String fileName) throws Exception {
        String path = getClass().getClassLoader().getResource(fileName).getPath();
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(path)));
        configKVMap.putAll((Map)prop);
    }

    public String getString(String key) {
        try {
            return getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getInt(String key) {
        try {
            return Integer.parseInt(getValue(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long getLong(String key) {
        try {
            return Long.parseLong(getValue(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean getBoolean(String key) {
        try {
            return Boolean.parseBoolean(getValue(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class ResourceReaderHolder {
        private static ResourceReader resourceReader = new ResourceReader();
    }
}
