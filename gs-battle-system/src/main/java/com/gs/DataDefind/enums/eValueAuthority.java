package com.gs.DataDefind.enums;

import lombok.val;

/**
 *  数值执行的权限
 */
public enum eValueAuthority {
    None(0),
    Value(1),
    Lower(1<<1),
    Upper(1<<2),
    ValueLower(1 | 1<<1),
    ValueUpper(1 | 1<<2),
    All(1 | 1<<1 | 1<<2);

    eValueAuthority(int val) {
        this.val = val;
    }
    private int val;

    public int getVal() {
        return val;
    }
}