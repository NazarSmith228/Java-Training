package org.java.training.spring.ioc.context.support;

import org.apache.commons.lang3.StringUtils;
import org.java.training.spring.ioc.annotation.IBean;

public class BeanNameResolver {

    public <T> String resolveBeanNameByType(Class<T> beanClass) {
        String valueFromAnnotation = tryGetNameFromAttributes(beanClass);
        if (StringUtils.isBlank(valueFromAnnotation)) {
            return StringUtils.uncapitalize(beanClass.getSimpleName());
        }
        return valueFromAnnotation;
    }

    private <T> String tryGetNameFromAttributes(Class<T> beanClass) {
        IBean annotation = beanClass.getAnnotation(IBean.class);
        return StringUtils.isBlank(annotation.value()) ? annotation.name() : annotation.value();
    }
}
