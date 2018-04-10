package com.gs.SkillHandler.CoreSkill;

import com.gs.SkillHandler.SkillBase;
import com.gs.SkillHandler.enums.eSkillType;
import com.gs.util.LGTimer;

public class ActiveSkill extends SkillBase {
    //继承属性
    protected float m_coolDown;
    protected float m_castTime;
    protected LGTimer m_timer;
    protected boolean m_ready;

    //构造函数
    public ActiveSkill(SkillCSVData data)//todo SkillCSVData issue
    {
        super(data, eSkillType.Active);
        m_timer = new LGTimer();
        m_ready = true;
    }

    //公共访问器
    public float castTime()
    {
        return m_castTime;
    }

    public void BeginCoolDown()
    {
        m_timer.ResetTime();
        m_timer.enable = true;
        m_timer.duration = m_coolDown;

        m_ready = false;
    }
    public void BeginCast()
    {
        m_timer.ResetTime();
        m_timer.enable = true;
        m_timer.duration = m_castTime;
    }

    public void ReSetTime()
    {
        m_timer.ResetTime();
    }

    public boolean CanCast()
    {
        if (0.0f==m_castTime)
        {
            return true;
        }

        return m_timer.eliminate;
    }

    public void Update()
    {
        if (!m_timer.eliminate)
        {
            m_timer.Update();
            if (!m_ready && m_timer.eliminate)
            {
                m_ready = true;
            }
        }
    }



    public boolean ready()
    {
       return m_ready;
    }

    @Override
    public void errorCallback(Object obj) {
        //todo skillBase issue 我用模板模式去替换skillBase下的delegate void ErrorCallback(object obj)方法，但是找不到这个委托方法的实现。目前置空

    }
}
