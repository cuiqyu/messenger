package com.limpid.messenger.global;

import com.limpid.messenger.advice.GlobalInternalExceptionHandler;
import org.springframework.context.annotation.Bean;

/**
 * 全局异常处理注射工厂
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 15:04
 */
public class GlobalExceptionHandlerInjectionFactory {

    @Bean
    public GlobalInternalExceptionHandler initGlobalInternalExceptionHandler() {
        return new GlobalInternalExceptionHandler();
    }

}
