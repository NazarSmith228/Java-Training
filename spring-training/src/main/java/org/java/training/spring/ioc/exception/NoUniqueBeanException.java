package org.java.training.spring.ioc.exception;

import org.springframework.beans.BeansException;

import static java.lang.String.format;

public class NoUniqueBeanException extends BeansException {

    private static final String defaultMessage = "Bean of %s not resolved. Multiple instances found";

    public NoUniqueBeanException(String msg) {
        super(format(defaultMessage, msg));
    }
}
