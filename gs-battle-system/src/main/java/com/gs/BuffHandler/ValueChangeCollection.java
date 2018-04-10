package com.gs.BuffHandler;

import com.gs.BuffHandler.enums.eBuffPriority;
import com.gs.DataDefind.enums.eValueAuthority;
import com.gs.DataDefind.enums.eValueType;

import java.util.HashMap;
import java.util.Map;

/**
 * Buff中用于管理一个数值对象中的所有数值变化对象更新
 * 文件位于\BattleSystem\BuffHandler下
 */
public class ValueChangeCollection {
    //继承属性
    protected int m_length;

    protected eValueType m_type;
    protected ValueChangeBufferCollection m_buffer;
    protected ValueChangePriorityController[] m_priorities;
    protected Map<eValueType, eBuffPriority> m_typePriority;


    //构造函数
    public ValueChangeCollection(eValueType type)
    {
        m_type = type;
        m_buffer = new ValueChangeBufferCollection();
        m_priorities = new ValueChangePriorityController[]
                {
                        new ValueChangePriorityController(eBuffPriority.Low),
                        new ValueChangePriorityController(eBuffPriority.Medium),
                        new ValueChangePriorityController(eBuffPriority.High)
                };
        m_length = m_priorities.length;
        m_typePriority = new HashMap<>();
    }

    //继承方法
    protected void _BuildBuffer()
    {
        m_buffer.BeginAdd();
        //重置
        m_typePriority.clear();

        for (int x = m_length - 1; x >= 0; x--)
        {
            for (ValueChange vc : m_priorities[x].m_valueChanges.values())
            {
                eBuffPriority buffPriority;
                buffPriority=m_typePriority.get(vc.getValueType());
                if (m_typePriority.containsKey(vc.getValueType()))
                {
                    if ((buffPriority.getVal() > vc.getM_belonger().getM_priority().getVal()))
                    {
                        continue;
                    }
                }

                m_typePriority.put(vc.valueType,vc.getM_belonger().getM_priority());//m_typePriority[vc.valueType] = vc.priority; todo 这样是否正确？
                m_buffer.Add(vc);
            }
        }
    }

    public float GetBuffedValue(eValueAuthority authority, float value)
    {
        return m_buffer.GetValue(authority, value);
    }

    public float GetRawValue(float value)
    {
        //没有处理任何数据, 等待文本出炉
        return value;
    }

    public void Destroy()
    {
        m_priorities = null;
    }

    public void AddValueChange(ValueChange vc)
    {
        m_priorities[vc.getM_belonger().getM_priority().getVal()].Add(vc);
        _BuildBuffer();
    }

    public void RemoveValueChange(ValueChange vc)
    {
        m_priorities[vc.getM_belonger().getM_priority().getVal()].Remove(vc);
        _BuildBuffer();
    }

    public void Refresh()
    {
        _BuildBuffer();
    }
}
