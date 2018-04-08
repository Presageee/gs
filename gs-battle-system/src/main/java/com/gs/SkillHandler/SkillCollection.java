package com.gs.SkillHandler;

import com.gs.SkillHandler.CoreSkill.ActiveSkill;
import com.gs.SkillHandler.CoreSkill.PassiveSkill;
import com.gs.util.LGTimer;

import java.util.*;

public class SkillCollection {
    protected Map<Integer, SkillBase> m_skillCollections;
    protected Queue<SkillBase> m_pendingSkill;
    protected ActiveSkill m_currentCastingSkill;
    protected boolean m_interrupt;
    protected List<SkillBase> m_queue;
    protected LGTimer m_gcdTimer;
    protected boolean m_inGcd;

    public SkillCollection()
    {
        m_currentCastingSkill = null;
        m_skillCollections = new HashMap<>();
        m_pendingSkill = new LinkedList<>();//Queue<SkillBase>();
        m_queue = new ArrayList<>();
        m_gcdTimer = new LGTimer();
        m_gcdTimer.ResetTime();
        m_gcdTimer.enable = true;
        m_gcdTimer.duration = 1.5f;
        m_interrupt = false;
        m_inGcd = false;
    }

    protected void _Add_To_Map(SkillBase skillBase)
    {
        m_skillCollections.put(skillBase.getM_skillID(),skillBase);//m_skillCollections[skillBase.skillID] = skillBase;
    }

    protected void _Handler_Cooldown()
    {
        for (SkillBase skillBase : m_skillCollections.values())
        {
            if (skillBase instanceof ActiveSkill)
            {
                ActiveSkill active = (ActiveSkill) skillBase;
                if (!active.ready())
                {
                    active.Update();
                }
            }
        }
    }

    protected void _Handler_Pending_Skill() {
        while (m_pendingSkill.size() > 0) {
            SkillBase skill = m_pendingSkill.poll();//Dequeue
            if (skill instanceof PassiveSkill) {
                skill.Handler();
            }
        }
    }

    public void AddSkill(SkillBase skillBase, Object obj)
    {

        if (skillBase == null)
        {
            return;
        }

        SkillBase skill = null;
        skill=m_skillCollections.get(skillBase.getM_skillID());
        if (!m_skillCollections.containsKey(skillBase.getM_skillID()))
        {
            _Add_To_Map(skillBase);
            skill = skillBase;
        }

        if (skill instanceof PassiveSkill)
        {
            m_pendingSkill.offer(skill);//Enqueue
        }
            else
        {
            if (m_inGcd)
            {
                return;
            }

            if (m_currentCastingSkill != null)
            {
                return;
            }

            ActiveSkill activeSkill = (ActiveSkill)skill;
            if (activeSkill.ready())
            {
//                    m_pendingSkill.Enqueue(skill);
                m_currentCastingSkill = activeSkill;
                m_currentCastingSkill.BeginCast();
            }
            else
            {
                //in cooldown
                activeSkill.HandlerError(obj);
            }
        }
    }

    public void Update()
    {

        //在GCD
        if (m_inGcd)
        {
            m_gcdTimer.Update();
            if (m_gcdTimer.eliminate)
            {
                m_inGcd = false;
                m_gcdTimer.ResetTime();
                return;
            }
        }

        //中断施法
        if (m_interrupt)
        {
            m_currentCastingSkill.ReSetTime();
            m_currentCastingSkill = null;
            m_interrupt = false;
        }

        //更新冷却时间
        _Handler_Cooldown();

        //更新当前施法
        if (m_currentCastingSkill != null)
        {
            m_currentCastingSkill.Update();
            if (m_currentCastingSkill.CanCast())
            {
                m_currentCastingSkill.Handler();
                m_currentCastingSkill.BeginCoolDown();
                m_currentCastingSkill = null;

                m_inGcd = true;
            }
        }

        //被动技能
        _Handler_Pending_Skill();
    }

    public void Clear()
    {
        m_skillCollections.clear();
    }

    public void Remove(SkillBase skillBase)
    {
        m_skillCollections.remove(skillBase.getM_skillID());
    }

    public void Destroy()
    {
        m_currentCastingSkill = null;
        m_pendingSkill.clear();
        m_queue.clear();
        m_skillCollections.clear();
    }

    public void InterruptCasting()
    {
        m_interrupt = true;
    }



}
