package com.gs.inner.sso.entity;

import com.gs.common.annotation.Timestamp;
import lombok.Data;

/**
 * author: theonelee
 * date: 2018/3/6
 */
@Data
public class Role {
    private Integer id;

    //private String roleName;

    private String accessUrl;//该role可以访问的url（如：/resource1,/resource2 就代表该角色可以访问http://ip:port/resource1/*和http://ip:port/resource2/*）

    @Timestamp
    private Long createTime;

    @Timestamp
    private Long lastUpdateTime;

    private Integer lastUpdateInnerUserId;

    private Integer locked;//标记删除位

}
