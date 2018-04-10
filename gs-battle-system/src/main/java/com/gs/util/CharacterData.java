package com.gs.util;

import com.gs.BuffHandler.Buff;
import com.gs.BuffHandler.BuffCollection;
import com.gs.DataDefind.BattleValue;
import com.gs.DataDefind.BattleValueCollection;
import com.gs.DataDefind.enums.eValueAuthority;
import com.gs.DataDefind.enums.eValueType;
import com.gs.VirusHandler.enums.eVirusType;

/**
 * 角色数值数据
 */
public class CharacterData extends BattleValueCollection {

    protected BattleValue m_hp = null;// 生命值
    protected BattleValue m_mp = null;
    protected BattleValue m_shield = null;
    protected BattleValue m_moveSpeed = null;
    protected BattleValue m_attack = null;
    protected BattleValue m_attackSpeed = null;
    protected BattleValue m_reduce_DamagePercentage = null;
    protected BattleValue m_reduce_ControlTime = null;
    protected BattleValue m_criticalHitRate = null;
    protected BattleValue m_criticalHitDamageRate = null;
    protected BattleValue m_drainLife = null;
    protected eVirusType m_adaptVirus;
    protected BuffCollection m_buffCollection;

    //构造函数
    public CharacterData()
    {
        m_hp = _AddData(eValueAuthority.All, eValueType.HP,0);
        m_mp = _AddData(eValueAuthority.All, eValueType.MP,0);
        m_shield = _AddData(eValueAuthority.ValueLower, eValueType.Shield,0);
        m_moveSpeed = _AddData(eValueAuthority.Value, eValueType.MoveSpeed,0);
        m_attack = _AddData(eValueAuthority.Value, eValueType.Attack,0);
        m_attackSpeed = _AddData(eValueAuthority.Value, eValueType.AttackSpeed,0);
        m_reduce_DamagePercentage = _AddData(eValueAuthority.Value, eValueType.ReduceDamagePercentage,0);
        m_reduce_ControlTime = _AddData(eValueAuthority.Value, eValueType.ReduceControlTime,0);
        m_criticalHitRate = _AddData(eValueAuthority.Value, eValueType.CriticalHitRate,0);
        m_criticalHitDamageRate = _AddData(eValueAuthority.Value, eValueType.CriticalHitDamageRate,0);
        m_drainLife = _AddData(eValueAuthority.Value, eValueType.DrainLife,0);
        m_buffCollection = new BuffCollection(this);
    }

    //公共访问器
    public boolean adaptVirus()// 角色是否匹配病毒种类
    {
        return ((getBelongVirus().getVal() & m_adaptVirus.getVal()) != eVirusType.None.getVal());
    }

    public eVirusType getBelongVirus()// 人物拥有的病毒状态类型
    {
        return m_adaptVirus;
        //set { m_adaptVirus = value; }
    }
    public void setBelongVirus(eVirusType value)
    {
        m_adaptVirus = value;
    }

    // 获取角色血量
    public float getHp()
    {
       return m_hp.getValue();
       // set { m_hp.value = value; }
    }
    public void setHp(float value)
    {
        m_hp.setValue(value,,);//m_hp.value = value;todo CharacterData issue 这时候的m_hp.value = value的语义是调用公共访问器的set方法，但是我从battleValue类上那边迁移过来时这个对应方法是有3个参数，而这边没有min和max的入参
    }
    public void setMaxHP(float value)
    {
        m_hp.setMax(value);//m_hp.max = value;
    }
    public void setMinHP(float value)
    {
        m_hp.setMin(value);//m_hp.min = value;
    }

    // 获取角色MP
    public float getMp()
    {
        return m_hp.getValue();
    }
    public void setMp(float value)
    {
        m_hp.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    // 护盾值
    public float getShield()
    {
        return m_shield.getValue();
    }
    public void setShield(float value)
    {
        m_shield.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    // 护盾值
    public float getMoveSpeed()
    {
        return m_moveSpeed.getValue();
    }
    public void setMoveSpeed(float value)
    {
        m_moveSpeed.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    // 获取角色修正后的攻击力
    public float getAttack()
    {
        return m_attack.getValue();
    }
    public void setAttack(float value)
    {
        m_attack.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }
    public void setMaxAttack(float value)
    {
        m_attack.setMax(value);
    }
    public void setMinAttack(float value)
    {
        m_attack.setMin(value);
    }

    // 攻速
    public float getAttackSpeed()
    {
        return m_attackSpeed.getValue();
    }
    public void setAttackSpeed(float value)
    {
        m_attackSpeed.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    // 减少受到伤害的比率
    public float getReduceDamagePercentage()
    {
        return m_reduce_DamagePercentage.getValue();
    }
    public void setReduceDamagePercentage(float value)
    {
        m_reduce_DamagePercentage.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    // 百分比减少自身受控制时间
    public float getReduceControlTime()
    {
        return m_reduce_ControlTime.getValue();
    }
    public void setReduceControlTime(float value)
    {
        m_reduce_ControlTime.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    // 暴击率
    public float getCriticalHitRate()
    {
        return m_criticalHitRate.getValue();
    }
    public void setCriticalHitRate(float value)
    {
        m_criticalHitRate.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    // 暴击伤害提升比率
    public float getCriticalHitDamageRate()
    {
        return m_criticalHitDamageRate.getValue();
    }
    public void setCriticalHitDamageRate(float value)
    {
        m_criticalHitDamageRate.setValue(value,,);//m_hp.value = value;todo CharacterData issue 同上
    }

    //公共方法
    public float LastDamageFilter(float damage)
    {
        //计算最终的Buff叠加数据, 此处需要解耦
        return damage;
    }
    public void AddBuff(Buff buff)
    {
        m_buffCollection.Add(buff);
    }
    public void RemoveBuff(Buff buff)
    {
        m_buffCollection.Remove(buff);
    }
    public void ClearBuff()
    {
        m_buffCollection.Clear();
    }
    public void Update()
    {
        m_buffCollection.Update();
    }
    @Override
    public void Destroy()
    {
        super.Destroy();
        m_buffCollection.Destroy();

        m_buffCollection = null;
        m_hp = null;
        m_mp = null;
        m_shield = null;
        m_moveSpeed = null;
        m_attack = null;
        m_attackSpeed = null;
        m_reduce_DamagePercentage = null;
        m_reduce_ControlTime = null;
        m_criticalHitRate = null;
        m_criticalHitDamageRate = null;
        m_drainLife = null;
    }
}
