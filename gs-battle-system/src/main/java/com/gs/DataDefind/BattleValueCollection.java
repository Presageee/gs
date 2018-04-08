package com.gs.DataDefind;

import com.gs.DataDefind.enums.eValueAuthority;
import com.gs.DataDefind.enums.eValueType;

import java.util.HashMap;
import java.util.Map;

public class BattleValueCollection {
    //继承属性
    protected Map<eValueType, BattleValue> m_map;

    //构造函数
    public BattleValueCollection()
    {
        m_map = new HashMap<>();
    }

    //继承方法
    protected BattleValue _AddData(eValueAuthority authority, eValueType type, float value)
    {
        //value = 0;
        BattleValue next = new BattleValue(authority, type, value);
        m_map.put(type, next);//m_map.Add(type, next);
        return next;
    }

    //公共方法
    public BattleValue GetBattleValue(eValueType type)
    {
        BattleValue buffer = null;
        buffer=m_map.get(type);//m_map.TryGetValue(type, out buffer);
        return buffer;
    }
    public void Destroy()
    {
        for (BattleValue dest : m_map.values())
        {
            dest.Destroy();
        }
        m_map = null;
    }
}
