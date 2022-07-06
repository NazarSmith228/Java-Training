package org.java.training.spring.ioc.beans;

import org.java.training.spring.ioc.annotation.IBean;

@IBean(name = "morningService")
public class MorningService implements GreetingService{

    @Override
    public void greeting() {
        System.out.println("Good morning!");
    }
}
