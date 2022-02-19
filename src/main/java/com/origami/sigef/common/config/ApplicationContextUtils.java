/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author User
 */
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext appContext;

    private ApplicationContextUtils() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return appContext.getBean(beanName);
    }
}
