package org.java.training.spring.ioc.context.core;

import java.util.Map;
import java.util.Set;

public interface IoCApplicationContext {

    <T> T getBean(Class<T> beanType);

    Object getBean(String beanName);

    <T> T getBean(String beanName, Class<T> beanType);

    <T> Map<String, T> getAllBeans(Class<T> beanType);

    Set<String> getBeanNames();
}
