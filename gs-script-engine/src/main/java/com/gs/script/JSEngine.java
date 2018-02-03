package com.gs.script;

import lombok.extern.slf4j.Slf4j;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by linjuntan on 2018/2/4.
 * email: ljt1343@gmail.com
 */
@Slf4j
public class JSEngine {
    private ScriptEngineManager scriptEngineManager;

    private ScriptEngine nashorn;

    private final static String JS_ENGINE_NASHORN = "nashorn";

    private Lock lock;

    public JSEngine() {
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        scriptEngineManager = new ScriptEngineManager();
        nashorn = scriptEngineManager.getEngineByName(JS_ENGINE_NASHORN);
        lock = new ReentrantLock();
    }


    /**
     * 执行js文件
     * @param scriptName js文件绝对路径
     * @param params 入参
     * @return 执行结果
     * @throws Exception js不存在
     */
    public Object doScript(String scriptName, Map<String, Object> params) throws Exception {
        File file = new File(scriptName);
        if (!file.isFile()) {
            throw new Exception(" >>> script no exists");
        }
        try {
            lock.lock();

            params.forEach((e, v) -> nashorn.put(e, v));
            try {
                return nashorn.eval(new FileReader(scriptName));
            } catch (Exception e) {
                log.error(" >>> execute script error", e);
                log.error(" >>> do script error => {}", scriptName);
            }

        } finally {
            lock.unlock();
        }

        return null;
    }

    /**
     * 执行js文件中的函数
     * @param scriptName js文件绝对路径
     * @param function 函数
     * @param params 入参
     * @return 执行结果
     * @throws Exception js不存在
     */
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

    //todo compile script, cache script
}
