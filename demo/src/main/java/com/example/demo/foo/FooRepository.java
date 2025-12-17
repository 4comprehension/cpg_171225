package com.example.demo.foo;

import org.springframework.stereotype.Component;

@Component
public class FooRepository implements Repository {
    @Override
    public String findUserById(int id) {
        return "foo";
    }
}
