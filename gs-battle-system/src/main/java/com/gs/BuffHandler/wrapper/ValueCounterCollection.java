package com.gs.BuffHandler.wrapper;

import lombok.Getter;
import lombok.Setter;

/**
 * 带有统计器的数值累加器 集合
 */
@Setter
@Getter
public class ValueCounterCollection {
    //继承属性
    protected int m_length;
    protected ValueCounter[] m_buffer;

    //构造函数
    public ValueCounterCollection(int length)
    {
        m_length = length;
        m_buffer = new ValueCounter[length];
        for (int x = 0; x < m_length; x++)
        {
            m_buffer[x] = new ValueCounter(0);
        }
    }

    public ValueCounterCollection(int length, int value){
        m_length = length;
        m_buffer = new ValueCounter[length];
        for (int x = 0; x < m_length; x++)
        {
            m_buffer[x] = new ValueCounter(0);
        }
    }

    //公共方法
    public void Reset(float value )
    {
        for (ValueCounter item : m_buffer)
        {
            item.Reset(value);
        }
    }
}
