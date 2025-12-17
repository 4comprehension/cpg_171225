package com.pivovarit.rental.foo;

import com.pivovarit.greeting.GreetingRepository;
import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.model.MovieDto;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.service.RentalService;
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
    public RentalService rentalService;

    @Test
    void shouldAddMovie() throws Exception {
        MovieAddRequest movieAddRequest = new MovieAddRequest(42, "Avengers", "NEW");

        rentalService.addMovie(movieAddRequest);
        assertThat(rentalService.findMovieById(new MovieId(42)))
          .map(MovieDto::id)
          .contains(42L);
    }

    @TestConfiguration
    static class Config {

        @Bean
        GreetingRepository greetingRepository() {
            return () -> "adsadasda";
        }
    }
}
