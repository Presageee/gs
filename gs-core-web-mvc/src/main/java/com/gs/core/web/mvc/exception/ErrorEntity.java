package com.gs.core.web.mvc.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * author: linjuntan
 * date: 2018/2/23
 */
@Data
@AllArgsConstructor
public class ErrorEntity {
    private String code;

    private String errMsg;

    private String payload;

    public ErrorEntity(String code, String errorMsg) {
        this.code = code;
        this.errMsg = errorMsg;
    }
}
