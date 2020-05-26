package com.limpid.messenger.annotation;

import java.lang.annotation.*;

/**
 * 日志鹰眼
 *
 * @auther cuiqiongyu
 * @create 2020/5/26 15:25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogHawkeye {

    /**
     * 方法描述
     *
     * @return
     */
    String methodDesc() default "";

}
