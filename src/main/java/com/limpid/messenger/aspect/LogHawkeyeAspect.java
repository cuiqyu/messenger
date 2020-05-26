package com.limpid.messenger.aspect;

import com.alibaba.fastjson.JSON;
import com.limpid.messenger.annotation.LogHawkeye;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志切面
 *
 * @auther cuiqiongyu
 * @create 2020/5/26 15:27
 */
@Aspect
@Component
public class LogHawkeyeAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogHawkeyeAspect.class);

    /**
     * 通过注解的形式匹配切入点
     */
    @Pointcut("@annotation(com.limpid.messenger.annotation.LogHawkeye)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {

    }

    /**
     * 创建环绕通知
     *
     * @return
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 请求开始的时间戳
        long startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        LogHawkeye annotation = method.getAnnotation(LogHawkeye.class);

        // 方法描述信息
        String methodDesc = annotation.methodDesc();
        logger.info("=====================请求开始=====================");
        // 请求链接
        logger.info("请求链接：{}", request.getRequestURL().toString());
        // 方法描述信息
        logger.info("方法描述信息：{}", methodDesc);
        // 请求类型
        logger.info("请求类型：{}", request.getMethod());
        // 请求方法
        logger.info("请求方法：{}", signature.getDeclaringTypeName(), signature.getName());
        // 请求IP
        logger.info("请求IP：{}", request.getRemoteAddr());
        // 请求入参
        logger.info("请求入参：{}", JSON.toJSONString(joinPoint.getArgs()));
        Object proceed = joinPoint.proceed();

        // 请求开始的时间戳
        long endTime = System.currentTimeMillis();
        // 请求耗时
        logger.info("请求耗时：{}ms", endTime - startTime);
        // 请求返回
        logger.info("请求返回：{}", JSON.toJSONString(proceed));
        logger.info("=====================请求结束=====================");
        return proceed;
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) {

    }

}
