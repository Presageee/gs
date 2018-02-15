package com.gs.script;

import java.util.Map;

/**
 * Created by linjuntan on 2018/2/4.
 * email: ljt1343@gmail.com
 */
public interface JScriptEngine {

    /**
     * 初始化
     */
    void init();

    /**
     * 销毁
     */
    void destroy();

    /**
     * 执行js文件
     * @param scriptName js文件绝对路径
     * @param params 入参
     * @return 执行结果
     * @throws Exception js不存在
     */
    Object doScript(String scriptName, Map<String, Object> params)  throws Exception;

    /**
     * 执行js文件中的函数
     * @param scriptName js文件绝对路径
     * @param function 函数
     * @param params 入参
     * @return 执行结果
     * @throws Exception js不存在
     */
    Object doScriptFunction(String scriptName, String function, Map<String, Object> params) throws Exception;
}
