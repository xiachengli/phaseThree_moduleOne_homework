package com.lagou.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GetBeanInstance implements ApplicationContextAware {


    private static ApplicationContext applicationContext ;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GetBeanInstance.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }



}
