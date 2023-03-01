package com.jyx.infra.spring.context;

import com.jyx.infra.spring.event.LocalApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author JYX
 * @since 2021/10/20 18:31
 */
@Component
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, ApplicationEventPublisherAware, DisposableBean {

    protected static ApplicationContext applicationContext = null;

    protected static ApplicationEventPublisher applicationEventPublisher = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        SpringContextHolder.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void destroy() {
        SpringContextHolder.applicationContext = null;
        SpringContextHolder.applicationEventPublisher = null;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static <T extends Annotation> Map<String, Object> getBeansWithAnnotation(Class<T> annotationType) {
        return applicationContext.getBeansWithAnnotation(annotationType);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <E extends LocalApplicationEvent> void publishEvent(E event) {
        applicationEventPublisher.publishEvent(event);
        if (log.isDebugEnabled()) {
            log.debug("Post application event is {}:{}", event.getId(), event);
        }

    }

}
