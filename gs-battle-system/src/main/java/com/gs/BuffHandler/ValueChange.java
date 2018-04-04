package com.gs.BuffHandler;

import com.gs.BuffHandler.enums.eSuperposeType;
import com.gs.DataDefind.enums.eValueAuthority;
import com.gs.DataDefind.enums.eValueType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueChange {
    //继承属性
    protected int m_identity = 0;
    protected Buff m_belonger = null;

    //公共属性
    public eSuperposeType superpose;
    public eValueType valueType;
    public eValueAuthority authority;
    public float value;

    public ValueChange( Buff m_belonger) {
        this.m_identity = hashCode();
        this.m_belonger = m_belonger;
    }

    public float value(){
      return getValue() * m_belonger.getM_level().getM_value();//todo IntWrapper issue
    }
}
