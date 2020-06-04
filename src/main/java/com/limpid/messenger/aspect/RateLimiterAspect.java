package com.limpid.messenger.aspect;

import com.aliyuncs.utils.StringUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.limpid.messenger.enumeration.GlobalConstant;
import com.limpid.messenger.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final String $EXPRESSION = "^\\$\\{(.*)\\}$";
    private static final String SPEL_EXPRESSION = "^\\#\\{(.*)\\}$";
    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Autowired
    private ApplicationContext applicationContext;

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
            String[] parameterNames = nameDiscoverer.getParameterNames(method);
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            // 获取指定限流的参数维度
            StringBuilder key = new StringBuilder(method.getName());
            int ratelimitInterval = 0;
            String ratelimitIntervalStr = spelParseValue(annotation.ratelimitIntervalSpel(), context);
            if (!StringUtils.isEmpty(ratelimitIntervalStr)) {
                try {
                    ratelimitInterval = Integer.valueOf(ratelimitIntervalStr);
                } catch (Exception e) {
                }
            }

            if (ratelimitInterval > 0) {
                String[] paramKeys = annotation.paramKeySpels();
                for (String paramKey : paramKeys) {
                    Object str;
                    if (null != (str = spelParseValue(paramKey, context))) {
                        paramKey = paramKey.replaceAll("^\\#\\{(.*)\\}$", "$1").replaceAll("^\\$\\{(.*)\\}$", "$1");
                        key.append("_").append(paramKey).append(":").append(str.toString());
                    }
                }

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
     * 解释spel表达式的值，包含解析${}和#{}表达式
     *
     * @param value
     * @param context
     * @return
     */
    private String spelParseValue(String value, EvaluationContext context) {
        try {
            // 判断value的类型
            if (value.matches(SPEL_EXPRESSION) || value.indexOf("#") == 0) { // spel表达式
                value = value.replaceAll(SPEL_EXPRESSION, "$1");
                if (value.matches($EXPRESSION)) {
                    return applicationContext.getEnvironment().getProperty(value.replaceAll($EXPRESSION, "$1"));
                } else {
                    return parser.parseExpression(value).getValue(context).toString();
                }
            }
            if (value.matches($EXPRESSION)) { // $占位符解析
                return applicationContext.getEnvironment().getProperty(value.replaceAll($EXPRESSION, "$1"));
            }
        } catch (Exception e) {
        }

        return value;
    }

}
