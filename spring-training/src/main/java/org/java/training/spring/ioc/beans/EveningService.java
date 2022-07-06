package org.java.training.spring.ioc.beans;

import org.java.training.spring.ioc.annotation.IBean;

@IBean(name = "eveningService")
public class EveningService implements GreetingService {

    @Override
    public void greeting() {
        System.out.println("Good evening!");
    }
}
