package com.limpid.messenger.annotation;

import com.limpid.messenger.global.GlobalHandlerImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 全局处理注解
 * z
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 14:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(GlobalHandlerImportBeanDefinitionRegistrar.class)
public @interface EnableGlobalHandler {

    /**
     * 是否处理返回值
     *
     * @return
     */
    boolean handlerReturnValue() default false;

    /**
     * 是否处理异常
     *
     * @return
     */
    boolean handlerException() default false;

}
