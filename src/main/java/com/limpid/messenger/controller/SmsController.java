package com.limpid.messenger.controller;

import com.limpid.messenger.annotation.InternalExceptionHandler;
import com.limpid.messenger.annotation.LogHawkeye;
import com.limpid.messenger.annotation.ResponseDataHandler;
import com.limpid.messenger.vo.SmsMessageVO;
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
public class SmsController {

    @PostMapping("/sendMessage")
    @ResponseDataHandler
    @LogHawkeye
    public Boolean sendMessage(@RequestBody SmsMessageVO smsMessageVO) {

        return false;
    }

}
