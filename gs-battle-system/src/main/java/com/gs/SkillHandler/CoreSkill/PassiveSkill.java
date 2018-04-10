package com.gs.SkillHandler.CoreSkill;

import com.gs.SkillHandler.SkillBase;
import com.gs.SkillHandler.enums.eSkillType;

public class PassiveSkill extends SkillBase {
    //构造函数
    public PassiveSkill(SkillCSVData data)
    {
        super(data, eSkillType.Passive);
    }


    @Override
    public void errorCallback(Object obj) {
        //todo skillBase issue
    }
}
