package com.pivovarit.domain.rental;

import com.pivovarit.domain.greeting.GreetingRepository;
import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SpringTest {

    @Autowired
    public RentalFacade rentalFacade;

    //    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldAddMovie() throws Exception {
        MovieAddRequest movieAddRequest = new MovieAddRequest(42, "Avengers", "NEW");

        rentalFacade.addMovie(movieAddRequest);
        assertThat(rentalFacade.findMovieById(new MovieId(42)))
          .map(MovieDto::id)
          .contains(42L);
    }

    @TestConfiguration
    static class Config {

        @Bean
        GreetingRepository greetingRepository() {
            return () -> "test";
        }

        @Bean
        InMemoryMovieRepository movieRepository() {
            return new InMemoryMovieRepository();
        }

        @Bean
        InMemoryMovieDescriptionsRepository movieDescriptionsRepository() {
            return new InMemoryMovieDescriptionsRepository();
        }
    }
}
