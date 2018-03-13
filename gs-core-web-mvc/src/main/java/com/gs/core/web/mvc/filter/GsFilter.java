package com.gs.core.web.mvc.filter;

import javax.servlet.Filter;

/**
 * Created by Lee on 2018/3/11 0011.
 */
public interface GsFilter extends Filter{
    /**
     * 获取filter拦截路径
     * @return 路径数组
     */
    public String[] getPathPatterns();
}
