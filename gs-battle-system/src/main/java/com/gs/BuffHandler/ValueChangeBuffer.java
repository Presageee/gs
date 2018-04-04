package com.gs.BuffHandler;

import com.gs.BuffHandler.enums.eBuffPriority;
import com.gs.BuffHandler.enums.eBuffType;
import com.gs.BuffHandler.enums.eSuperposeType;
import com.gs.BuffHandler.wrapper.ValueCounter;
import com.gs.BuffHandler.wrapper.ValueCounterCollection;

/**
 * Buff中用于高速处理缓存值变化的类对象
 */
public class ValueChangeBuffer {
    protected final static int s_multiplyValueCount = 2;

    //继承属性
    protected boolean m_changeTo = false;
    protected boolean m_initialization = false;

    protected ValueChange m_lastChangeToCache;
    protected ValueCounter m_changeToValues;
    protected ValueCounter m_plusValues;
    protected ValueCounterCollection m_multiplyValues;

    //构造函数
    public ValueChangeBuffer()
    {
        m_changeToValues = new ValueCounter(0);
        m_plusValues = new ValueCounter(0);
        m_multiplyValues = new ValueCounterCollection(s_multiplyValueCount, 1);

    }

    //继承方法
    protected void _Internal_AddSuperposition(ValueChange vc, eSuperposeType vs)
    {
        if (vs == eSuperposeType.Multiply)
        {
            if (vc.getM_belonger().getM_type() == eBuffType.Common)
            {
                m_multiplyValues.getM_buffer()[0].Add(vc.value());
            }
            else if (vc.getM_belonger().getM_type() == eBuffType.Special)
            {
                m_multiplyValues.getM_buffer()[1].Add(vc.value());
            }
        }
        else if (vs == eSuperposeType.Plus)
        {
            m_plusValues.Add(vc.value());
        }
    }
    protected void _Internal_Add(ValueChange vc, eSuperposeType vs)
    {
        if (m_changeTo)
        {
            //非设值
            if (vs != eSuperposeType.ChangeTo)
            {
                return;
            }

            //未添加过设值buff
            if (m_lastChangeToCache == null)
            {
                m_changeToValues.Add(vc.value);
                m_lastChangeToCache = vc;
                return;
            }

            eBuffPriority last = m_lastChangeToCache.getM_belonger().getM_priority();
            eBuffPriority now = vc.getM_belonger().getM_priority();

            //优先级低
            if (last.compareTo(now) > 0)
            {
                return;
            }

            //缓存buff优先级低
            if (last.compareTo(now) < 0)
            {
                m_changeToValues.Reset(0);
                m_changeToValues.Add(vc.value);
                m_lastChangeToCache = vc;
                return;
            }

            //同优先级较后生效的buff
            if (m_lastChangeToCache.getM_belonger().getM_timer().beginTimeStamp() < vc.getM_belonger().getM_timer().beginTimeStamp())
            {
                m_changeToValues.Reset(0);
                m_changeToValues.Add(vc.value);
                m_lastChangeToCache = vc;
            }

//                if (vs == eSuperposeType.ChangeTo && m_changeToValues.count == 0)
//                {
//                    m_changeToValues.Add(vc.value);
//                }
        }
        else
        {
            _Internal_AddSuperposition(vc, vs);
        }
    }

    //公共方法
    public void BeginAdd()
    {
        m_initialization = false;
        m_plusValues.Reset(0);
        m_changeToValues.Reset(0);
        m_multiplyValues.Reset(1);
        m_lastChangeToCache = null;
    }
    public void Add(ValueChange value)
    {
        if (value == null)
        {
            return;
        }
        eSuperposeType vs = value.superpose;
        if (vs == eSuperposeType.ChangeTo)
        {
            m_changeTo = true;
        }
//            if (m_initialization == false)
//            {
//                m_initialization = true;
//                m_changeTo = (vs == eSuperposeType.ChangeTo);
//            }
        _Internal_Add(value, vs);
    }
    public float GetBuffedValue(float value)
    {
        /*
         * 普通和普通叠加的时候, 直接相加
         * 特殊和特殊叠加的时候, 直接相加
         *
         * 普通和特殊叠加的时候, 相乘
         */
        if (m_changeTo)
        {
            if (m_changeToValues.getM_count() > 0)
            {
                return m_changeToValues.getM_value();
            }
        }
        else
        {
            value += m_plusValues.getM_value();

            for (int x = 0; x < s_multiplyValueCount; x++)
            {
                value *= m_multiplyValues.getM_buffer()[x].getM_value();
            }
            return value;
        }
        return value;
    }
}
