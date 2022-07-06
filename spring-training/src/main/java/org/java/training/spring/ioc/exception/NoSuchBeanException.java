package org.java.training.spring.ioc.exception;

import org.springframework.beans.BeansException;

import static java.lang.String.format;

public class NoSuchBeanException extends BeansException {

    private static final String DEFAULT_MESSAGE = "Bean of %s not found";

    public NoSuchBeanException(String msg) {
        super(format(DEFAULT_MESSAGE, msg));
    }
}
