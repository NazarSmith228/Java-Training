package org.java.training.spring.ioc.exception;

import org.springframework.beans.BeansException;

import static java.lang.String.format;

public class ExistingBeanException extends BeansException {

    private static final String DEFAULT_MESSAGE = "Registry failed. Bean with name %s already exists";

    public ExistingBeanException(String msg) {
        super(format(DEFAULT_MESSAGE, msg));
    }
}
