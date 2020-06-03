package com.limpid.messenger.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.limpid.messenger.enumeration.GlobalConstant;
import com.limpid.messenger.exception.CustomException;
import com.limpid.messenger.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 *
 * @auther cuiqiongyu
 * @create 2020/6/2 16:08
 */
@Component
@Aspect
@Slf4j
public class RateLimiterAspect {

    private static final ConcurrentHashMap<String, RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private SpringUtil springUtil;

    /**
     * 通过注解的形式配置切入点
     */
    @Pointcut("@annotation(com.limpid.messenger.annotation.RateLimiter)")
    public void rateLimit() {
    }

    /**
     * 环绕通知
     *
     * @param point
     * @return
     */
    @Around(value = "rateLimit()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 根据方法签名获取方法
        Method method = signature.getMethod();
        // 获取方法上的 RateLimiter 注解
        com.limpid.messenger.annotation.RateLimiter annotation = method.getAnnotation(com.limpid.messenger.annotation.RateLimiter.class);
        if (null != annotation) {
            // 获取参数列表
            Object[] args = point.getArgs();
            // 获取字段名称列表
            ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
            String[] parameterNames = pnd.getParameterNames(method);
            Map<String, Object> paramMap = new HashMap<>();
            for (int i = 0; i < parameterNames.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }

            // 获取指定限流的参数维度
            StringBuilder key = new StringBuilder(method.getName());
            int timeout = annotation.timeout();
            TimeUnit timeUnit = annotation.timeUnit();
            int ratelimitInterval = 0;
            String ratelimitIntervalStr = parseValue(annotation.ratelimitInterval(), JSON.toJSONString(paramMap));
            if (!StringUtils.isEmpty(ratelimitIntervalStr)) {
                try {
                    ratelimitInterval = Integer.valueOf(ratelimitIntervalStr);
                } catch (Exception e) {
                }
            }
            String[] paramKeys = annotation.paramKeys();
            for (String paramKey : paramKeys) {
                Object str;
                if (null != (str = parseValue(paramKey, JSON.toJSONString(paramMap)))) {
                    paramKey = paramKey.replaceAll("^\\#\\{(.*)\\}$", "$1").replaceAll("^\\$\\{(.*)\\}$", "$1");
                    key.append("_").append(paramKey).append(":").append(str.toString());
                }
            }

            if (ratelimitInterval > 0) {
                // 尝试访问令牌
                if (null == RATE_LIMITER_CACHE.get(key.toString())) {
                    RATE_LIMITER_CACHE.put(key.toString(), RateLimiter.create(BigDecimal.ONE.divide(new BigDecimal(ratelimitInterval), 12, BigDecimal.ROUND_HALF_UP).doubleValue()));
                }
                if (RATE_LIMITER_CACHE.get(key.toString()) != null && !RATE_LIMITER_CACHE.get(key.toString()).tryAcquire(annotation.timeout(), annotation.timeUnit())) {
                    throw new CustomException(GlobalConstant.ResponseState.FREQUENCY_TOO_FAST);
                }
            }
        }
        return point.proceed();
    }

    /**
     * 解释value值，包含解析${}和#{}表达式
     *
     * @param value
     * @param paramMapJson
     * @return
     */
    private String parseValue(String value, String paramMapJson) {
        // 判断value的类型
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        Environment environment = springUtil.getApplicationContext().getEnvironment();
        // 处理${}的引用
        if (value.matches("^\\$\\{(.*)\\}$")) {
            value = environment.getProperty(value.replaceAll("^\\$\\{(.*)\\}$", "$1"));
            return value;
        }
        if (value.matches("^\\#\\{(.*)\\}$")) {
            String valueContext = value.replaceAll("^\\#\\{(.*)\\}$", "$1");
            // 处理#引用类型
            if (!StringUtils.isEmpty(paramMapJson)) {
                JSONObject jsonObject = JSON.parseObject(paramMapJson);
                String[] split = valueContext.split("\\.");
                for (int i = 0; i < split.length - 1; i++) {
                    if (null == (jsonObject = JSON.parseObject(JSON.toJSONString(jsonObject.get(split[i]))))) {
                        return value;
                    }
                }
                valueContext = split[split.length - 1];
                value = jsonObject.getString(valueContext);
            }
        }
        return value;
    }

}
