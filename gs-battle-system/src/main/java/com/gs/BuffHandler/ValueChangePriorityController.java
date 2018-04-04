package com.gs.BuffHandler;

import com.gs.BuffHandler.enums.eBuffPriority;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 数值优先级控制器
 */
@Setter
@Getter
public class ValueChangePriorityController {
    //继承属性
    protected eBuffPriority m_priority;
    protected ValueChange m_lastValueChange;
    protected Map<Integer, ValueChange> m_valueChanges;

    //构造函数
    public ValueChangePriorityController(eBuffPriority priority)
    {
        m_priority = priority;
        m_lastValueChange = null;
        m_valueChanges = new HashMap<Integer, ValueChange>();
    }
    //继承方法
    protected void _GetLastValueChange()
    {
        long lastStamp = 0;
        m_lastValueChange = null;
        for (ValueChange item : m_valueChanges.values())
        {
            long timestamp = timestamp(item);
            if (lastStamp < timestamp)
            {
                lastStamp = timestamp;
                m_lastValueChange = item;
            }
        }
    }
    protected int _Compare(ValueChange left, ValueChange right)
    {
        if (timestamp(left) < timestamp(right))
        {
            return -1;
        }
        else if (timestamp(left) == timestamp(right))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    //公共方法
    public void Add(ValueChange value)
    {
        m_valueChanges.put(value.getM_identity(),value);
        _GetLastValueChange();
    }
    public void Remove(ValueChange value)
    {
        if (m_valueChanges.containsKey(value.getM_identity()))
        {
            m_valueChanges.remove(value.getM_identity());
            if (m_lastValueChange == null &&
                    m_lastValueChange.getM_identity() == value.getM_identity())
            {
                _GetLastValueChange();
            }
        }
    }

    private long timestamp(ValueChange item){
        return item.getM_belonger().m_timer.beginTimeStamp();
    }

}
