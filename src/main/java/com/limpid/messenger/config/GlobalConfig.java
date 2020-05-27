package com.limpid.messenger.config;

import com.limpid.messenger.annotation.EnableGlobalHandler;
import com.limpid.messenger.cache.GuavaCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 全局处理配置
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 15:35
 */
@Configuration
public class GlobalConfig {

    @Autowired
    private AliSmsConfig aliSmsConfig;

    @EnableGlobalHandler(handlerException = true, handlerReturnValue = true)
    public static class GlobalHandler {
    }

    @Bean("verificationCodeSendTimeCache")
    public GuavaCache<Long> verificationCodeSendTimeCache() {
        return new GuavaCache<Long>(10000, aliSmsConfig.getVerificationCodeInterval(), TimeUnit.SECONDS);
    }

}
