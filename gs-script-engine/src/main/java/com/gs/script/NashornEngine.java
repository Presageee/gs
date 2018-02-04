package com.gs.script;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by linjuntan on 2018/2/4.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class NashornEngine implements JScriptEngine {
    private final static String JS_ENGINE_NASHORN = "nashorn";

    private ScriptEngineManager scriptEngineManager;

    private ScriptEngine nashorn;

    private boolean cachedScript = true;

    private Map<String, CompiledScript> scriptMap = new HashMap<>(8);

    private String[] initParams = new String[]{"-doe", "--global-per-engine"};

    private Lock lock;

    public NashornEngine() {
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        scriptEngineManager = new ScriptEngineManager();
        NashornScriptEngineFactory factory = null;
        for (ScriptEngineFactory f : scriptEngineManager.getEngineFactories()) {
            if (f.getEngineName().equalsIgnoreCase("Oracle Nashorn")) {
                factory = (NashornScriptEngineFactory)f;
                break;
            }
        }

        if (factory == null) {
            throw new RuntimeException(" >>> java version not support nashorn");
        }

        nashorn = factory.getScriptEngine(initParams);

        lock = new ReentrantLock();
    }

    @Override
    public void destroy() {
        nashorn = null;
    }


    /**
     * 执行js文件
     * @param scriptName js文件绝对路径
     * @param params 入参
     * @return 执行结果
     * @throws Exception js不存在
     */
    @Override
    public Object doScript(String scriptName, Map<String, Object> params) throws Exception {
        File file = new File(scriptName);
        if (!file.isFile()) {
            throw new Exception(" >>> script no exists");
        }

        if (cachedScript && scriptMap.containsKey(scriptName)) {
            return execute(scriptMap.get(scriptName), params);
        }

        CompiledScript cs = ((Compilable)nashorn).compile(new FileReader(scriptName));
        if (cachedScript) {
            scriptMap.put(scriptName, cs);
        }

        return execute(cs, params);
    }

    /**
     * 执行js文件中的函数
     * @param scriptName js文件绝对路径
     * @param function 函数
     * @param params 入参
     * @return 执行结果
     * @throws Exception js不存在
     */
    @Override
    public Object doScriptFunction(String scriptName, String function, Map<String, Object> params) throws Exception {
        File file = new File(scriptName);
        if (!file.isFile()) {
            throw new Exception();
        }

        try {
            lock.lock();

            List<Object> objs = new ArrayList<>();
            params.forEach((e, v) -> objs.add(v));

            nashorn.eval(new FileReader(scriptName));
            Invocable invocable = (Invocable) nashorn;

            try {
                return invocable.invokeFunction(function, objs.toArray());
            } catch (Exception e) {
                log.error(" >>> execute script error", e);
                log.error(" >>> do script error => {}", scriptName);
                log.error(" >>> param => {}", objs.toString());
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    /**
     * open cache script, default true
     * @param cachedScript boolean
     */
    public void setCachedScript(boolean cachedScript) {
        this.cachedScript = cachedScript;
    }

    private Object execute(CompiledScript cs, Map<String, Object> params) {
        try {
            lock.lock();
            Bindings binds = new SimpleBindings();
            binds.put("params", params);

            return cs.eval(binds);
        } catch (Exception e) {
            log.error(" >>> execute script error", e);
        } finally {
            lock.unlock();
        }
        return null;
    }

}
