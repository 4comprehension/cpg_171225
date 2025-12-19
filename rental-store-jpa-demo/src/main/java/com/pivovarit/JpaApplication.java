package com.pivovarit;

import com.pivovarit.domain.rental.MovieRepository;
import com.pivovarit.domain.rental.PersistedRentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    @Component
    static class Runner implements ApplicationRunner {

        private final MovieRepository movieRepository;
        private final PersistedRentalRepository rentalRepository;

        Runner(MovieRepository movieRepository, PersistedRentalRepository rentalRepository) {
            this.movieRepository = movieRepository;
            this.rentalRepository = rentalRepository;
        }

        @Override
        public void run(ApplicationArguments args) {
        }
    }
}
