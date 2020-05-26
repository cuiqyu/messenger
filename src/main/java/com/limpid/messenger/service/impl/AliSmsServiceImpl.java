package com.limpid.messenger.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.utils.StringUtils;
import com.limpid.messenger.config.AliSmsConfig;
import com.limpid.messenger.exception.CustomException;
import com.limpid.messenger.service.AliSmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public CommonResponse sendMessage(String cellphone, Integer templateCodeType, String templateParam) {
        return null;
    }

    @Override
    public CommonResponse sendBatchMessage(List<String> cellphones, Integer templateCodeType, String templateParam) {
        return null;
    }

    @Override
    public CommonResponse sendVerificationCode(String cellphone, Integer templateCodeType) {
        // TODO Assert

        // 获取配置文件中短信模板类型对应的短信模板id
        String templateCode;
        Map<Integer, String> templateCodeMap = aliSmsConfig.getTemplateCodeMap();
        if (templateCodeMap.isEmpty() || StringUtils.isEmpty(templateCode = templateCodeMap.get(templateCodeType))) {
            logger.error("发送短信失败，未找到对应的短信模板配置类型：templateCodeType：{}", templateCodeType);
            throw new CustomException("未找到对应的短信模板配置类型");
        }

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                aliSmsConfig.getAccessKeyId(), aliSmsConfig.getAccessKeySecret());
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
        // 短信模板填充数据 TODO
        request.putQueryParameter("TemplateParam", "TODO");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            logger.error("阿里云发送短信服务异常，e：{}", e);
            throw new CustomException("TODO");
        } catch (ClientException e) {
            logger.error("阿里云发送短信client异常，e：{}", e);
            throw new CustomException("TODO");
        }

        return null;
    }

}
