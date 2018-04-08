package com.gs.DataDefind;

import com.gs.BuffHandler.ValueChange;
import com.gs.BuffHandler.ValueChangeCollection;
import com.gs.DataDefind.enums.eValueAuthority;
import com.gs.DataDefind.enums.eValueType;
import com.gs.util.data.ValueWrapper;

public class BattleValue extends ValueWrapper {
    //继承属性
    protected eValueType m_type;
    protected eValueAuthority m_authority;
    protected ValueChangeCollection m_changeCollection;

    //构造函数
    public BattleValue(eValueAuthority authority, eValueType type, float value)
    {
        super(value);
        m_type = type;
        m_authority = authority;
        m_changeCollection = new ValueChangeCollection(type);
    }

    //公共访问器
    //获取混合后的属性

    @Override
    public float getValue() {
        return m_changeCollection.GetBuffedValue(eValueAuthority.Value, m_value);
    }

    @Override
    public void setValue(float value, float min, float max) {
        float next = m_changeCollection.GetRawValue(value);
        float lower = _AuthorityMatch(m_authority, eValueAuthority.Lower) ? min : Float.MIN_VALUE;
        float upper = _AuthorityMatch(m_authority, eValueAuthority.Upper) ? max : Float.MAX_VALUE;
        //m_value = Mathf.Clamp(next, lower, upper);
        if (next<lower)
            m_value= lower;
        if (next>upper)
            m_value= upper;
        m_value= next;
    }

    @Override
    public float getMax() {
        return m_changeCollection.GetBuffedValue(eValueAuthority.Upper, m_max_value);
    }

    @Override
    public void setMax(float value) {
        if (_AuthorityMatch(m_authority, eValueAuthority.Upper))
        {
           m_max_value = m_changeCollection.GetRawValue(value);
        }
    }

    @Override
    public float getMin() {
        return m_changeCollection.GetBuffedValue(eValueAuthority.Lower, m_min_value);
    }

    @Override
    public void setMin(float value) {
        if (_AuthorityMatch(m_authority, eValueAuthority.Lower))
        {
            m_min_value = m_changeCollection.GetRawValue(value);
        }
    }

    //继承方法
    protected boolean _AuthorityMatch(eValueAuthority self, eValueAuthority target)
    {
        return (self.getVal() & target.getVal()) != eValueAuthority.None.getVal();
    }

    //公共方法
    public void AddValueChange(ValueChange value)
    {
        m_changeCollection.AddValueChange(value);
    }
    public void RemoveValueChange(ValueChange value)
    {
        m_changeCollection.RemoveValueChange(value);
    }
    public void Refresh()
    {
        m_changeCollection.Refresh();
    }
    public void Destroy()
    {
        m_changeCollection.Destroy();
        m_changeCollection = null;
    }
}
