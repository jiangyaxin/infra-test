package com.jyx.infra.spring.pipeline;

import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author jiangyaxin
 * @since 2023/11/3 21:08
 */
@Slf4j
@Component
public class PipelineHolder implements ApplicationContextAware {

    interface Constants {
        String BEAN_SUFFIX = "#Executor";
    }

    private ApplicationContext applicationContext;


    public void registerPipeline(Class<?> clazz, BeanDefinition beanDefinition) {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();

        beanDefinitionRegistry.registerBeanDefinition(generateBeanName(clazz), beanDefinition);

        Logs.debug(log, "Registered pipeline bean definition: {}", clazz.getName());
    }

    public SpringPipelineExecutor getPipeline(Class<?> clazz) {
        Object bean = applicationContext.getBean(generateBeanName(clazz));
        return (SpringPipelineExecutor) bean;
    }

    private String generateBeanName(Class<?> clazz) {
        return clazz.getName() + Constants.BEAN_SUFFIX;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
