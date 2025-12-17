package com.example.demo.bar;

import org.springframework.stereotype.Component;

import java.lang.classfile.attribute.ModuleResolutionAttribute;

@Component
public class BarService {

    private final BarRepository repository;

    public BarService(BarRepository repository) {
        this.repository = repository;
    }

    public void hello() {
        System.out.println("hello from BarService");
    }
}
