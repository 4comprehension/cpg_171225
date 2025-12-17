package com.example.demo.bar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BarConfiguration {

    @Bean
    public BarRepository barRepository() {
        return new BarRepository();
    }

    @Bean
    public BarService barService(BarRepository repository) {
        return new BarService(repository);
    }
}
