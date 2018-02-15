package com.gs.script;

import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by linjuntan on 2018/2/4.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class V8Engine implements JScriptEngine {
    private static final String ENGINE_ALIAS = "V8-%s";

    private static AtomicInteger v8Counter = new AtomicInteger(0);

    private String alias;

    private V8 runtime;

    private boolean cacheScript = true;

    private Map<String, String> scriptMap = new HashMap<>();

    public V8Engine() {
        init();
    }

    public void init() {
        alias = String.format(ENGINE_ALIAS, v8Counter.incrementAndGet());
        log.info(" >>> v8 engine create, alias => {}", alias);

        runtime = V8.createV8Runtime(alias);
    }

    @Override
    public void destroy() {
        log.info(" >>> v8 engine destroy, alias => {}", alias);
        runtime.release();
    }


    //todo v8 js

    @Override
    public Object doScript(String scriptName, Map<String, Object> params) throws Exception {
        String script = getScript(scriptName);

        V8Object paramObj = null;
        try {
            paramObj = V8ObjectUtils.toV8Object(runtime, params);

            V8Object result = runtime.executeObjectScript(script);
            return V8ObjectUtils.toMap(result);
        } catch (Exception e) {
            log.error(" >>> v8 engine execute script error", e);
        } finally {
            release(paramObj);
        }

        return null;
    }

    @Override
    public Object doScriptFunction(String scriptName, String function, Map<String, Object> params) throws Exception {
        return null;
    }

    private String getScript(String scriptName) {
        if (cacheScript && scriptMap.containsKey(scriptName)) {
            return scriptMap.get(scriptName);
        }

        try {
            String script = new String(Files.readAllBytes(Paths.get(scriptName)), Charset.forName("UTF-8"));
            if (cacheScript) {
                scriptMap.putIfAbsent(scriptName, script);
            }
            return script;
        } catch (IOException e) {
            log.error(" >>> read script error", e);
        }

        return null;
    }

    private void release(Releasable... releasable) {
        Arrays.stream(releasable).forEach(e -> {
            if (e != null) {
                e.release();
            }
        });
    }

}
