package com.limpid.messenger.advice;

import com.limpid.messenger.annotation.ResponseDataHandler;
import com.limpid.messenger.enumeration.GlobalConstant;
import com.limpid.messenger.vo.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局返回值处理
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 16:14
 */
public class GlobalReturnValueHandler implements HandlerMethodReturnValueHandler {

    private static class LRUCacheMap<K, V> extends LinkedHashMap {

        private Integer MAX_CACHE_NUM = 100; // 缓存的最大数量 默认100

        LRUCacheMap() {
            // 构造一个LinkedHashMap，并设置排序为访问排序
            new LinkedHashMap<K, V>(16, 0.75f, true);
        }

        LRUCacheMap(int initialCapacity) {
            // 构造一个LinkedHashMap，并设置排序为访问排序
            new LinkedHashMap<K, V>(initialCapacity, 0.75f, true);
            MAX_CACHE_NUM = initialCapacity * 2;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_CACHE_NUM;
        }
    }

    private LRUCacheMap<MethodParameter, ResponseDataHandler> supportResponseAnnotation;
    private HandlerMethodReturnValueHandler handlerMethodReturnValueHandler;

    public GlobalReturnValueHandler(HandlerMethodReturnValueHandler handlerMethodReturnValueHandler, RequestMappingHandlerMapping handlerMapping) {
        this.handlerMethodReturnValueHandler = handlerMethodReturnValueHandler;
        this.supportResponseAnnotation = new LRUCacheMap<>(handlerMapping.getHandlerMethods().size());
    }

    /**
     * 是否支持统一返回处理
     *
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        if (!supportResponseAnnotation.containsKey(methodParameter)) {
            ResponseDataHandler responseDataHandler = null;
            if (AnnotatedElementUtils.isAnnotated(methodParameter.getMethod(), ResponseDataHandler.class.getName())) {
                responseDataHandler = methodParameter.getMethod().getDeclaredAnnotation(ResponseDataHandler.class);
            } else if (AnnotatedElementUtils.isAnnotated(methodParameter.getClass(), ResponseDataHandler.class.getName())) {
                responseDataHandler = methodParameter.getClass().getDeclaredAnnotation(ResponseDataHandler.class);
            }
            if (null != responseDataHandler) {
                supportResponseAnnotation.put(methodParameter, responseDataHandler);
            }
        }
        return handlerMethodReturnValueHandler.supportsReturnType(methodParameter) || hasAnnotation(methodParameter);
    }

    /**
     * 处理返回值
     *
     * @param o
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @throws Exception
     */
    @Override
    public void handleReturnValue(@Nullable Object o, MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        if (hasAnnotation(methodParameter)) {
            if (!(o instanceof ResponseData)) {
                o = new ResponseData<>(GlobalConstant.ResponseState.SUCCESS, o);
            }
        }
        handlerMethodReturnValueHandler.handleReturnValue(o, methodParameter, modelAndViewContainer, nativeWebRequest);
    }

    private Boolean hasAnnotation(MethodParameter returnType) {
        return supportResponseAnnotation.containsKey(returnType) || supportResponseAnnotation.get(returnType) != null;
    }

}
