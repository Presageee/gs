package com.gs.util.data;

import lombok.Getter;
import lombok.Setter;

/**
 * todo
 * 文件处于\Assets\Client\ClientCommonSystem\Data下
 */
@Setter
@Getter
public class IntWrapper {
    //继承属性属性
    protected int m_value;
    protected int m_min_value;
    protected int m_max_value;

    public IntWrapper(int m_value, int m_min_value, int m_max_value) {
        this.m_value = m_value;
        this.m_min_value = m_min_value;
        this.m_max_value = m_max_value;
    }

    public IntWrapper() {
    }


}
