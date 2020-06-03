package com.limpid.messenger.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.utils.StringUtils;
import com.limpid.messenger.annotation.RateLimiter;
import com.limpid.messenger.cache.VerificationCodeSendTimeCacheService;
import com.limpid.messenger.config.AliSmsConfig;
import com.limpid.messenger.enumeration.GlobalConstant;
import com.limpid.messenger.exception.CustomException;
import com.limpid.messenger.service.AliSmsService;
import com.limpid.messenger.util.CustomExceptionAssert;
import com.limpid.messenger.util.ExceptionUtil;
import com.limpid.messenger.vo.client.ali.AliSengSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 短信服务
 *
 * @author cuiqiongyu
 * @date 2020-05-26 22:43
 */
@Service
public class AliSmsServiceImpl implements AliSmsService {

    private final static Logger logger = LoggerFactory.getLogger(AliSmsServiceImpl.class);

    @Autowired
    private AliSmsConfig aliSmsConfig;

    @Autowired
    private VerificationCodeSendTimeCacheService verificationCodeSendTimeCache;

    @Override
    public CommonResponse sendMessage(String cellphone, Integer templateCodeType, String templateParam) {
        CustomExceptionAssert.notNull(cellphone, GlobalConstant.ResponseState.CELLPHONE_NOT_NULL);
        CustomExceptionAssert.notNull(templateCodeType, GlobalConstant.ResponseState.TEMPLATE_TYPE_NOT_NULL);

        // 获取配置文件中短信模板类型对应的短信模板id
        String templateCode;
        Map<Integer, String> templateCodeMap = aliSmsConfig.getTemplateCodeMap();
        if (templateCodeMap.isEmpty() || StringUtils.isEmpty(templateCode = templateCodeMap.get(templateCodeType))) {
            logger.error("发送短信失败，未找到对应的短信模板配置类型：templateCodeType：{}", templateCodeType);
            throw new CustomException(GlobalConstant.ResponseState.NOT_FOUND_SMS_TEMPLATE_TYPE);
        }

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliSmsConfig.getAccessKeyId(), aliSmsConfig.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", cellphone);
        request.putQueryParameter("SignName", aliSmsConfig.getSignName());
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", templateParam);

        try {
            CommonResponse response = client.getCommonResponse(request);
            if (Integer.valueOf(200).compareTo(response.getHttpStatus()) != 0) {
                logger.error("阿里云发送短信服务异常，response：{}", JSON.toJSONString(response));
                throw new CustomException(GlobalConstant.ResponseState.SMS_SEND_FAIL);
            }
            return response;
        } catch (ServerException e) {
            logger.error("阿里云发送短信服务异常，e：{}", ExceptionUtil.getDesc(e));
            throw new CustomException(GlobalConstant.ResponseState.SMS_SEND_FAIL);
        } catch (ClientException e) {
            logger.error("阿里云发送短信client异常，e：{}", ExceptionUtil.getDesc(e));
            throw new CustomException(GlobalConstant.ResponseState.SMS_SEND_FAIL);
        }
    }

    @Override
    public CommonResponse sendBatchMessage(List<String> cellphones, Integer templateCodeType, String templateParam) {
        return null;
    }

    @Override
    @RateLimiter(ratelimitInterval = "${sms.ali.verificationCodeInterval}", timeout = 200, paramKeys = {"#{cellphone}"})
    public String sendVerificationCode(String cellphone) {
        CustomExceptionAssert.notEmpty(cellphone, GlobalConstant.ResponseState.CELLPHONE_NOT_NULL);
        // 生成指定位数的随机验证码
        Integer verificationCodeLength = aliSmsConfig.getVerificationCodeLength();
        // 默认6位数
        verificationCodeLength = (null == verificationCodeLength) ? 6 : verificationCodeLength;
        Random random = new Random();
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < verificationCodeLength; i++) {
            code.append(random.nextInt(9));
        }

        // 发送短信验证码
        Map<String, String> templateParamMap = new HashMap<>();
        templateParamMap.put("code", code.toString());
        CommonResponse commonResponse = sendMessage(cellphone, 1, JSON.toJSONString(templateParamMap));
        AliSengSmsResponse aliSengSmsResponse = JSON.parseObject(commonResponse.getData(), AliSengSmsResponse.class);
        if (null == aliSengSmsResponse) {
            throw new CustomException(GlobalConstant.ResponseState.SMS_SEND_FAIL);
        }
        if (!"OK".equals(aliSengSmsResponse.getCode())) {
            throw new CustomException(aliSengSmsResponse.getCode(), aliSengSmsResponse.getMessage());
        }

        return code.toString();
    }

}
