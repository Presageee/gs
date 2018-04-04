package com.gs.BuffHandler.wrapper;

import lombok.Getter;
import lombok.Setter;

/**
 * 带有统计器的数值累加器
 */
@Getter
@Setter
public class ValueCounter {
    //继承属性
    protected float m_value=0;
    protected int m_count;

    //构造函数
    public ValueCounter(float value)
    {
        m_value = value;
        m_count = 0;
    }

    //公共方法
    public void Reset(float v)
    {
        m_value = v;
        m_count = 0;
    }
    public void Add(float v)
    {
        m_value += v;
        m_count++;
    }
}
