package com.example.demo.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HelloWorldConfiguration {

    @Bean
    public HelloWorldService helloWorldService(@Value("${application.greeting}") String greeting) {
        return new HelloWorldService(greeting);
    }
}
