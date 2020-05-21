package com.limpid.messenger.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 返回状态定义
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 15:20
 */
public interface GlobalConstant {

    @AllArgsConstructor
    @Getter
    enum ResponseState {
        SUCCESS(0, "成功"),
        PARAM_ERROR(1, "参数错误"),
        FAIL(10, "失败"),
        BUSINESS_ERROR(99, "业务异常"),
        SYSTEM_ERROR(100, "系统异常");
        private int code;
        private String message;
    }

}
