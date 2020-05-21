package com.limpid.messenger.global;

import com.limpid.messenger.annotation.EnableGlobalHandler;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * 全局统一处理服务BeanDefinition注册器
 *
 * @auther cuiqiongyu
 * @create 2020/5/20 14:51
 */
public class GlobalHandlerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (annotationMetadata.isAnnotated(EnableGlobalHandler.class.getName())) {
            // 获取注解的属性内容Map
            Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableGlobalHandler.class.getName());
            // 全局处理返回值
            if (Boolean.TRUE.equals(annotationAttributes.get("handlerReturnValue"))) {
                beanDefinitionRegistry.registerBeanDefinition(GlobalReturnValueInjectionFactory.class.getName(),
                        BeanDefinitionBuilder.rootBeanDefinition(GlobalReturnValueInjectionFactory.class).getBeanDefinition());
            }
            // 全局处理异常
            if (Boolean.TRUE.equals(annotationAttributes.get("handlerException"))) {
                beanDefinitionRegistry.registerBeanDefinition(GlobalExceptionHandlerInjectionFactory.class.getName(),
                        BeanDefinitionBuilder.rootBeanDefinition(GlobalExceptionHandlerInjectionFactory.class).getBeanDefinition());
            }
        }
    }

}
