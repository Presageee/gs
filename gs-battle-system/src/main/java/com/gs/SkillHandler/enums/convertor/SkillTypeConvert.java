package com.gs.SkillHandler.enums.convertor;

import com.gs.SkillHandler.enums.eSkillType;

import java.util.HashMap;
import java.util.Map;

/**
 * 技能类型转换器
 */
public final class SkillTypeConvert {
    //私有静态属性
    private static boolean s_inited = false;
    private static Map<eSkillType, String> s_typeMap;
    private static Map<String, eSkillType> s_nameMap;

    //静态私有方法
    private static void Initialization()
    {
        s_typeMap = new HashMap<>();
        s_nameMap = new HashMap<>();

        //int max = eSkillType.Max;
        for (eSkillType type:eSkillType.values())
        {
            //eSkillType type = (eSkillType)x;
            String name = type.name();
            String lower = name.toLowerCase();
            s_typeMap.put(type,name);//s_typeMap[type] = name;
            s_nameMap.put(lower,type);//s_nameMap[lower] = type;
        }

        s_inited = true;
    }

    //静态公共方法
    public static eSkillType GetType(String name)
    {
        if (s_inited == false)
        {
            Initialization();
        }
        eSkillType type = eSkillType.Max;
        String lower = name.toLowerCase();
        type=s_nameMap.get(lower);
        return type;
    }
    public static String GetName(eSkillType type)
    {
        if (s_inited == false)
        {
            Initialization();
        }
        String name = null;
        name= s_typeMap.get(type);
        return name;
    }
}
