package com.gs.common.util;

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

        if (!CommonUtils.isNull(expired)) {
            builder.append(";Max-Age=")
                    .append(expired);
        }

        if (!CommonUtils.isNull(domain)) {
            builder.append(";Domain=")
                    .append(domain);
        }

        if (!CommonUtils.isNull(path)) {
            builder.append(";Path=")
                    .append(path);
        }

        return builder.toString();
    }
}
