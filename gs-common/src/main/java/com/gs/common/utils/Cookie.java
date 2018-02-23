package com.gs.common.utils;

import lombok.Data;

/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
@Data
public class Cookie {
    private String name;

    private String domain;

    private String value;

    private Long expired;

    private String path;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(name)
                .append("=")
                .append(value);

        if (!CommonUtil.isNull(expired)) {
            builder.append(";Max-Age=")
                    .append(expired);
        }

        if (!CommonUtil.isNull(domain)) {
            builder.append(";Domain=")
                    .append(domain);
        }

        if (!CommonUtil.isNull(path)) {
            builder.append(";Path=")
                    .append(path);
        }

        return builder.toString();
    }
}
