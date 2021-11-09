package com.jyx.infra.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

/**
 * @author JYX
 * @since 2021/10/20 18:31
 */
@Component
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, ApplicationEventPublisherAware,DisposableBean {

    private  static ApplicationContext applicationContext=null;

    private static ApplicationEventPublisher applicationEventPublisher=null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext=applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        SpringContextHolder.applicationEventPublisher=applicationEventPublisher;
    }

    @Override
    public void destroy() {
        SpringContextHolder.applicationContext=null;
        SpringContextHolder.applicationEventPublisher=null;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static  <E extends ApplicationEvent> void publishEvent(E event){
        applicationEventPublisher.publishEvent(event);
        if(log.isDebugEnabled()){
            return;
        }
        log.debug("发布Application事件{}",event);
    }

}
