package com.gs.util.data;

/**
 * todo
 * 文件处于\Assets\Client\ClientCommonSystem\Data下
 */
public class ValueWrapper {
    //继承属性属性
    protected float m_value;
    protected float m_min_value;
    protected float m_max_value;

    //构造函数
    public ValueWrapper(float value) {
        m_value = value;
        m_min_value = value;
        m_max_value = value;
    }

    public ValueWrapper(float value, float max) {
        m_value = value;
        m_min_value = 0;
        m_max_value = max;
    }

    public ValueWrapper(float value, float min, float max) {
        m_value = value;
        m_min_value = min;
        m_max_value = max;
    }

    //公共访问器
    public void setValue(float value,float min,float max){
        if (value<min)
            m_value= min;
        if (value>max)
            m_value= max;
        m_value= value;
    }

    public float getValue(){
        return m_value;
    }

    public float getMax(){
        return m_max_value;
    }

    public void setMax(float value){
        m_max_value=value;
        if (m_value>m_max_value){
            m_value=m_max_value;
        }
    }

    public float getMin(){
        return m_min_value;
    }

    public void setMin(float value){
        m_min_value=value;
        if (m_value<m_min_value){
            m_value=m_min_value;
        }
    }


    //公共方法
    public void Set(float value)
    {
        m_value = m_max_value = m_min_value = value;
    }

    public void Dispose()
    {
        m_value = m_min_value = m_max_value = 0;
    }


}
