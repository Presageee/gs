package com.gs.script;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by linjuntan on 2018/2/4.
 * email: ljt1343@gmail.com
 */
public class JSEngineTest {
    @Test
    public void doScript() throws Exception {
        NashornEngine engine = new NashornEngine();
        Map<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 2);

        Object sum = null;

        for (int i = 0; i < 100; i++) {
            long time = System.currentTimeMillis();
            sum = engine.doScript("E:\\workspace\\gs\\gs-script-engine\\src\\test\\resources\\add.js"
                    , params);
            System.out.println(System.currentTimeMillis() - time + "  " + sum);
        }
        Assert.assertEquals(((Double) sum).intValue(), 3);
        engine.destroy();
    }

    @Test
    public void doScriptFunction() throws Exception {
        NashornEngine engine = new NashornEngine();
        Map<String, Object> params = new HashMap<>();
        params.put("a", 1);
        params.put("b", 2);
        Object sum = engine.doScriptFunction("E:\\workspace\\gs\\gs-script-engine\\src\\test\\resources\\addFunction.js"
                , "add", params);
        Assert.assertEquals(((Double) sum).intValue(), 3);
    }

}
