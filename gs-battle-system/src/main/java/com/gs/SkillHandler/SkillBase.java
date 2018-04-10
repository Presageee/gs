package com.gs.SkillHandler;

import com.gs.SkillHandler.enums.eSkillType;
import lombok.Getter;

@Getter
public abstract class SkillBase {
    //继承属性
    protected float m_value;
    protected float m_requireMP;
    protected int m_elementID;
    protected eSkillType m_skillType;
    protected int m_skillID;
    protected SkillCSVData m_data;//todo SkillCSVData issue 文件在Client\GameProcess\CharacterSystem
    //protected ErrorCallback error;

    //构造函数
    public SkillBase(SkillCSVData data, eSkillType type)
    {
        m_data = data;
        m_value = data.m_value;
        m_requireMP = data.m_mp;
        m_elementID = data.m_elementID;
        m_skillID = data.m_id;
        m_skillType = type;
    }

    public void Handler()
    {

    }
    public void HandlerError(Object obj)
    {
        errorCallback(obj);
    }

    public void Update()
    {

    }

    public abstract void errorCallback(Object obj);
}
