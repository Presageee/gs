package com.gs.script;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by linjuntan on 2018/2/5.
 * email: ljt1343@gmail.com
 */

public class V8EngineTest {
    @Test
    public void doScript() throws Exception {
        V8Engine v8Engine = new V8Engine();

        Map<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 2);

        v8Engine.doScript("E:\\workspace\\gs\\gs-script-engine\\src\\test\\resources\\add.js"
                , params);
        v8Engine.destroy();
    }

    @Test
    public void doScriptFunction() throws Exception {
    }



}
