package com.pivovarit.moviedescriptions;

import com.pivovarit.moviedescriptions.client.RentalStoreClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class MovieDescriptionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieDescriptionsApplication.class, args);
    }
    
    @Component
    static record Runner(RentalStoreClient client) implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) {
        }
    }
}
