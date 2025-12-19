package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryFakeTest {

    private RentalFacade rentalFacade;

    @BeforeEach
    void setUp() {
        rentalFacade = inMemoryInstance();
    }

    @RepeatedTest(1_000)
    void shouldAddMovie() {
        rentalFacade.addMovie(new MovieAddRequest(42, "Avengers", "NEW"));
        assertThat(rentalFacade.findMovieById(new MovieId(42)))
          .map(MovieDto::id)
          .contains(42L);
    }

    private static RentalFacade inMemoryInstance() {
        InMemoryMovieRepository movieRepository = new InMemoryMovieRepository();
        return new RentalFacade(movieRepository, new InMemoryMovieDescriptionsRepository(), new MovieRentalService(new InMemoryRentalHistory(), movieRepository));
    }
}
