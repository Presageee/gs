package com.gs.BuffHandler;

import com.gs.BuffHandler.enums.eSuperposeType;
import com.gs.DataDefind.enums.eValueAuthority;
import com.gs.DataDefind.enums.eValueType;

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

    public int getM_identity() {
        return m_identity;
    }

    public void setM_identity(int m_identity) {
        this.m_identity = m_identity;
    }

    public Buff getM_belonger() {
        return m_belonger;
    }

    public void setM_belonger(Buff m_belonger) {
        this.m_belonger = m_belonger;
    }

    public eSuperposeType getSuperpose() {
        return superpose;
    }

    public void setSuperpose(eSuperposeType superpose) {
        this.superpose = superpose;
    }

    public eValueType getValueType() {
        return valueType;
    }

    public void setValueType(eValueType valueType) {
        this.valueType = valueType;
    }

    public eValueAuthority getAuthority() {
        return authority;
    }

    public void setAuthority(eValueAuthority authority) {
        this.authority = authority;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
