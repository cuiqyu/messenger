package com.limpid.messenger.controller;

import com.limpid.messenger.annotation.InternalExceptionHandler;
import com.limpid.messenger.annotation.LogHawkeye;
import com.limpid.messenger.annotation.ResponseDataHandler;
import com.limpid.messenger.service.AliSmsService;
import com.limpid.messenger.vo.SmsMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信服务
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 14:36
 */
@RestController
@RequestMapping("/sms")
@InternalExceptionHandler
@Api(value = "短信接口")
public class SmsController {

    @Autowired
    private AliSmsService aliSmsService;

    @PostMapping("/sendMessage")
    @ResponseDataHandler
    @ApiOperation(value = "验证码", notes = "根据手机号发送验证码")
    // @LogHawkeye
    public String sendMessage(@RequestBody SmsMessageVO smsMessageVO) {
        return aliSmsService.sendVerificationCode(smsMessageVO.getCellphone());
    }

}
