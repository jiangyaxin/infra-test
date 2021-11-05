package com.jyx.infra.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author JYX
 * @since 2021/10/20 18:31
 */
@Component
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private  static ApplicationContext applicationContext=null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext=applicationContext;
    }

    @Override
    public void destroy() {
        SpringContextHolder.applicationContext=null;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static String getYmlProperty(String name){
        return applicationContext.getEnvironment().getProperty(name);
    }
}
