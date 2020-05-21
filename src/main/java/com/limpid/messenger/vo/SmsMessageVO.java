package com.limpid.messenger.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * 短信发送内容对象
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 14:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessageVO implements Serializable {

    private static final long serialVersionUID = -1729197048626129573L;

    /**
     * 接受短信的手机号
     */
    @NonNull
    private String cellphone;
    /**
     * 发送的短信内容
     */
    @NonNull
    private String messageContext;

}
