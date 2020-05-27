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
        BUSINESS_ERROR(50, "业务异常"),
        NOT_FOUND_SMS_TEMPLATE_TYPE(51, "未找到对应的短信模板配置类型"),
        SMS_SEND_FAIL(52, "短信发送失败，请稍后再试"),
        VERIFICATION_CODE_ALL_TOO_OFTEN(53, "您发送的短信验证码过于频繁，请稍后在试"),
        CELLPHONE_NOT_NULL(54, "手机号不能为空"),
        TEMPLATE_TYPE_NOT_NULL(55, "短信模板类型不能为空"),
        SYSTEM_ERROR(100, "系统异常");

        private int code;
        private String message;
    }

}
