package com.gs.BuffHandler.enums;

/**
 * Buff的执行优先级
 */
public enum eBuffPriority {
    Low(0),Medium(1),High (2);

    eBuffPriority(int val) {
        this.val = val;
    }

    private int val;

    public int getVal() {
        return val;
    }
}
