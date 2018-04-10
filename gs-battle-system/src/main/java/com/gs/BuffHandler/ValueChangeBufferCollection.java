package com.gs.BuffHandler;

import com.gs.DataDefind.enums.eValueAuthority;

import java.util.HashMap;
import java.util.Map;

/**
 * Buff中用于高速处理缓存值变化的类对象集合
 * 用于区分不同的访问权限
 */
public class ValueChangeBufferCollection {
    //继承属性
    protected ValueChangeBuffer m_lower = new ValueChangeBuffer();
    protected ValueChangeBuffer m_value = new ValueChangeBuffer();
    protected ValueChangeBuffer m_upper = new ValueChangeBuffer();
    protected Map<eValueAuthority, ValueChangeBuffer> m_bufferMap;

    //构造函数
    public ValueChangeBufferCollection()
    {
        m_bufferMap = new HashMap<>();
        m_bufferMap.put(eValueAuthority.Lower, m_lower);
        m_bufferMap.put(eValueAuthority.Value, m_value);
        m_bufferMap.put(eValueAuthority.Upper, m_upper);
    }

    //公共方法
    public void BeginAdd()
    {
        for (ValueChangeBuffer buff : m_bufferMap.values())
        {
            buff.BeginAdd();
        }
    }
    public void Add(ValueChange vc)
    {
        for (eValueAuthority k : m_bufferMap.keySet())
        {
            if ((k.getVal() & vc.getAuthority().getVal()) != eValueAuthority.None.getVal())
            {
                m_bufferMap.get(k).Add(vc);
            }
        }
    }
    public float GetValue(eValueAuthority authority, float value)
    {
        return m_bufferMap.get(authority).GetBuffedValue(value);
    }

}
