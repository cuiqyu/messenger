package com.limpid.messenger.global;

import com.limpid.messenger.advice.GlobalReturnValueHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局返回值处理注射工厂
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 15:04
 */
public class GlobalReturnValueInjectionFactory implements ApplicationContextAware, InitializingBean {

    /**
     * {@link RequestMappingHandlerAdapter}
     */
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * {@link RequestMappingHandlerMapping}
     */
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();
        handlerMethodReturnValueHandlers.addAll(requestMappingHandlerAdapter.getReturnValueHandlers());
        for (HandlerMethodReturnValueHandler handler : handlerMethodReturnValueHandlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                handlerMethodReturnValueHandlers.set(handlerMethodReturnValueHandlers.indexOf(handler), new GlobalReturnValueHandler(handler, requestMappingHandlerMapping));
                break;
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(handlerMethodReturnValueHandlers);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        requestMappingHandlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
    }

}
