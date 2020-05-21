package com.limpid.messenger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内部异常注解
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 15:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InternalExceptionHandler {

}
