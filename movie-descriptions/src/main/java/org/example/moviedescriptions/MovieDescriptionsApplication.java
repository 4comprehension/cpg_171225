package org.example.moviedescriptions;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MovieDescriptionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieDescriptionsApplication.class, args);
    }

    @Component
    static class Runner implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) {
        }
    }
}

