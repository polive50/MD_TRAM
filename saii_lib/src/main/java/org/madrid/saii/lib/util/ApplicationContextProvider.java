package org.madrid.saii.lib.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{
    
    @Setter
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        setContext(ac);
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }
    
    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
    
}
