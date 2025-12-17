package com.example.demo.bar;

import com.example.demo.foo.Repository;

public class BarRepository implements Repository {
    @Override
    public String findUserById(int id) {
        return "bar";
    }
}
