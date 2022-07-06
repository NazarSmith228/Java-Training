package org.java.training.spring.ioc.beans;

import org.java.training.spring.ioc.annotation.IBean;

@IBean
public class AfternoonService implements GreetingService{

    @Override
    public void greeting() {
        System.out.println("Good afternoon!");
    }
}
