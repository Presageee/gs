package com.gs.core.web.exception;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
@Data
public class BaseWebException extends RuntimeException {
    private Map<String, String> msgMap = new HashMap<>();

    public BaseWebException(String code, String msg) {
        msgMap.putIfAbsent("code", code);
        msgMap.putIfAbsent("msg", msg);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(msgMap);
    }
}
