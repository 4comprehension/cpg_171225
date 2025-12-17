package com.example.demo.foo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FooService {

    private final Repository repository;

    public FooService(@Qualifier("fooRepository") Repository repository) {
        this.repository = repository;
    }

    public void hello() {
        System.out.println("hello from FooService!");
    }
}
