package org.java.training.spring.ioc.exception;

import org.springframework.beans.BeansException;

public class BeanCreationException extends BeansException {

    public BeanCreationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
