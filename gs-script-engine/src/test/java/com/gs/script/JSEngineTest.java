package com.gs.script;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by linjuntan on 2018/2/4.
 * email: ljt1343@gmail.com
 */
public class JSEngineTest {
    @Test
    public void doScript() throws Exception {
        JSEngine engine = new JSEngine();
        Map<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 2);
        Object sum = engine.doScript("E:\\workspace\\gs\\gs-script-engine\\src\\test\\resources\\add.js"
                , params);
        Assert.assertEquals(((Double) sum).intValue(), 3);
    }

    @Test
    public void doScriptFunction() throws Exception {
        JSEngine engine = new JSEngine();
        Map<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 2);
        Object sum = engine.doScriptFunction("E:\\workspace\\gs\\gs-script-engine\\src\\test\\resources\\addFunction.js"
                , "add", params);
        Assert.assertEquals(((Double) sum).intValue(), 3);
    }

}
