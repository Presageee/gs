package com.gs.BuffHandler;

import com.gs.DataDefind.BattleValue;
import com.gs.DataDefind.BattleValueCollection;
import com.gs.DataDefind.enums.eValueType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Buff状态机, 用于负责处理角色的数值
 */
@Slf4j
public class BuffCollection {
    //继承属性
    protected List<Integer> m_destroy;
    protected Map<Integer,Buff> m_timingBuffs;
    protected Map<Integer,Buff> m_untimingBuffs;
    protected BattleValueCollection m_valueCollection;

    //构造函数
    public BuffCollection(BattleValueCollection values)
    {
        m_destroy = new ArrayList<>();
        m_timingBuffs = new HashMap<>();
        m_untimingBuffs = new HashMap<>();
        m_valueCollection = values;
    }

    //继承方法
    protected Map<Integer,Buff> _Internal_GetMap(Buff buff)
    {
        return buff.isM_timing() ? m_timingBuffs : m_untimingBuffs;
    }
    protected void _Internal_RefreshBattleValue(Buff buff)
    {
        //刷新混合队列缓冲
        for (eValueType type : buff.getM_valueTypes())
        {
            BattleValue target = m_valueCollection.GetBattleValue(type);
            if (target == null)
            {
                log.debug("没有找到当前数值信息." + type);
                return;
            }
            target.Refresh();
        }
    }
    protected void _Internal_AddValueChanges(Buff buff)
    {
        for (ValueChange item : buff.getM_valueChanges())
        {
            BattleValue target = m_valueCollection.GetBattleValue(item.valueType);
            if (target == null)
            {
                log.debug("没有找到当前数值信息." + item.valueType);
                return;
            }
            target.AddValueChange(item);
        }
    }
    protected void _Internal_AddConflicted(Map<Integer,Buff> map, Buff buff)
    {
        Buff conflicted = null;
        for (int idx : buff.getM_conflictIndecies())
        {

            if (map.containsKey(idx))
            {
                conflicted= map.get(idx);
                break;
            }
        }
        if (conflicted != null)
        {
            buff.SetLevel(conflicted.getM_level().getM_value() + 1);
            Remove(conflicted);
        }
    }
    protected void _Internal_Add(Map<Integer,Buff> map, Buff buff)
    {
        //写入buff列表
        Buff buffer = null;
        if (map.containsKey(buff.getM_buffID()))
        {
            buffer= map.get(buff.getM_buffID());
            //增加buff层数
            buffer.AddLevel();

            //刷新数据
            _Internal_RefreshBattleValue(buff);
        }
        else
        {
            //加入buff刷新队列
            map.put(buff.getM_buffID(), buff);

            //加入到混合队列
            _Internal_AddValueChanges(buff);
        }
    }
    protected void _Internal_Remove(Buff buff)
    {
        //从混合队列中移除
        for(ValueChange item : buff.getM_valueChanges())
        {
            BattleValue target = m_valueCollection.GetBattleValue(item.valueType);
            if (target == null)
            {
                log.error("没有找到当前数值信息." + item.valueType);
                return;
            }
            target.RemoveValueChange(item);
        }
    }


    //公共方法
    public void Add(Buff buff)
    {
        Map map = _Internal_GetMap(buff);
        _Internal_AddConflicted(map, buff);
        _Internal_Add(map, buff);
    }
    public void Remove(Buff buff)
    {
        //从buff列表中移除
        Map map = _Internal_GetMap(buff);
        map.remove(buff.getM_buffID());
        _Internal_Remove(buff);
    }
    public void Update()
    {
        for(Buff item : m_timingBuffs.values())
        {
            if (item.eliminate())
            {
                m_destroy.add(item.getM_buffID());
            }
            else
            {
                item.Update();
            }
        }
        if (m_destroy.size() > 0)
        {
            for(int id : m_destroy)
            {
                // m_timingBuffs.Remove(id);
                Buff item = null;
                if (m_timingBuffs.containsKey(id))
                {
                    item=m_timingBuffs.get(id);
                    Remove(item);
                }
            }
            m_destroy.clear();
        }
    }
    public void Clear()
    {
        for(Buff buff : m_timingBuffs.values())
        {
            _Internal_Remove(buff);
        }
        for(Buff buff : m_untimingBuffs.values())
        {
            _Internal_Remove(buff);
        }
        m_timingBuffs.clear();
        m_untimingBuffs.clear();
    }
    public void Destroy()
    {
        m_valueCollection.Destroy();
        m_valueCollection = null;
        m_destroy = null;
        m_timingBuffs = null;
        m_untimingBuffs = null;
    }

}
