package com.gs.BuffHandler;

import com.gs.BuffHandler.enums.eBuffPriority;
import com.gs.BuffHandler.enums.eBuffType;
import com.gs.DataDefind.enums.eValueType;
import com.gs.util.data.IntWrapper;
import com.gs.util.LGTimer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Buff {
    /// 身份标识
    private int m_identity;

    /// Buff的ID值
    protected int m_buffID;

    /// 是否计时
    protected boolean m_timing;

    /// 计时器
    protected LGTimer m_timer;

    /// 叠加层数的值
    protected IntWrapper m_level;

    /// Buff种类
    protected eBuffType m_type;

    /// Buff优先级
    protected eBuffPriority m_priority;

    // 会修改到目标的数据集合
    protected List<ValueChange> m_valueChanges;

    // 会修改到的目标数据类型集合
    protected List<eValueType> m_valueTypes;

    // 冲突BUFF类型
    protected List<Integer> m_conflictIndecies;


    //构造函数
    public Buff() {
        m_buffID = 0;
        m_identity = hashCode();
        m_valueChanges = new ArrayList<ValueChange>();
        m_conflictIndecies = new ArrayList<Integer>();
        m_timer = new LGTimer();
        m_timer.ResetTime();
        m_level = new IntWrapper(1, 1, 1);
    }

    public boolean eliminate() {
        if (m_timing) {
            return m_timer.eliminate;
        }
        return false;

    }

    //公共方法
    public void ResetTime() {
        m_timer.ResetTime();
    }

    public void Update() {
        if (m_timer.eliminate == false) {
            m_timer.Update();
        }
    }

    public void AddLevel() {
        m_level.setM_value(m_level.getM_value() + 1);//m_level.value++;//todo IntWrapper issue
        ResetTime();
    }

    public void SetLevel(int level) {
        m_level.setM_value(level);//m_level.value = level;//todo IntWrapper issue
        ResetTime();
    }

}
