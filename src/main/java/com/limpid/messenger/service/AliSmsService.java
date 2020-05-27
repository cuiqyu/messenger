package com.limpid.messenger.service;

import com.aliyuncs.CommonResponse;

import java.util.List;

/**
 * 短信服务
 *
 * @author cuiqiongyu
 * @date 2020-05-26 22:43
 */
public interface AliSmsService {

    /**
     * @param cellphone        发送的手机号
     * @param templateCodeType 发送短信的模板类型
     * @param templateParam    发送短信模板的变量参数，json格式
     * @return com.aliyuncs.CommonResponse
     * @description 阿里云发送单条短信
     * @author cuiqiongyu
     * @date 23:29 2020-05-26
     **/
    CommonResponse sendMessage(String cellphone, Integer templateCodeType, String templateParam);

    /**
     * @param cellphones       发送的手机号列表
     * @param templateCodeType 发送短信的模板类型
     * @param templateParam    发送短信模板的变量参数，json格式
     * @return com.aliyuncs.CommonResponse
     * @description 阿里云批量发送短信
     * @author cuiqiongyu
     * @date 23:33 2020-05-26
     **/
    CommonResponse sendBatchMessage(List<String> cellphones, Integer templateCodeType, String templateParam);

    /**
     * @param cellphone 发送的手机号
     * @return com.aliyuncs.CommonResponse
     * @description 给用户发送短信验证码
     * @author cuiqiongyu
     * @date 23:05 2020-05-26
     **/
    String sendVerificationCode(String cellphone);


}
