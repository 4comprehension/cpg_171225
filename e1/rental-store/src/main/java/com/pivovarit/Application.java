package com.pivovarit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 1. podpiąć springa
// 2. dependency injection z adnotacjami (RentalService.findAllMovies)
// 3. dependency injection z Java config (RentalService.findAllMovies)
// mvn spring-boot:run
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
