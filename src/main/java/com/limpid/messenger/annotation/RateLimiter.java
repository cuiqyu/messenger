package com.limpid.messenger.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解（支持${}和SPEL表达式）
 *
 * @auther cuiqiongyu
 * @create 2020/6/2 16:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    double NOT_LIMITED = 0.0;

    /**
     * 请求的频率间隔spel表达式，单位s
     */
    String ratelimitIntervalSpel() default "0";

    /**
     * 超时时长
     */
    int timeout() default 0;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 限流的参数维度
     *
     * @return
     */
    String[] paramKeySpels() default {};

}
