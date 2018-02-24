package com.gs.core.web.mvc.exception;

import com.alibaba.fastjson.JSON;
import lombok.Data;

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
        msgMap.putIfAbsent("errMsg", msg);
    }

    public ErrorEntity toErrorEntity() {
        return new ErrorEntity(msgMap.get("code"), msgMap.get("errMsg"));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(msgMap);
    }
}
