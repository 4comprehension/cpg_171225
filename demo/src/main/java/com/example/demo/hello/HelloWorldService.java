package com.example.demo.hello;

import org.springframework.stereotype.Component;

public class HelloWorldService {

    private final String greeting;

    public HelloWorldService(String greeting) {
        this.greeting = greeting;
    }

    public void hello() {
        System.out.println(greeting);
    }
}
