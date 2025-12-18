package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class InMemoryFakeTest {

    @RepeatedTest(1_000)
    void shouldAddMovie() {
        var rentalService = inMemoryInstance();
        MovieAddRequest movieAddRequest = new MovieAddRequest(42, "Avengers", "NEW");

        rentalService.addMovie(movieAddRequest);
        Assertions.assertThat(rentalService.findMovieById(new MovieId(42)))
          .map(MovieDto::id)
          .contains(42L);
    }

    private static RentalFacade inMemoryInstance() {
        InMemoryMovieRepository movieRepository = new InMemoryMovieRepository();
        return new RentalFacade(movieRepository, new InMemoryMovieDescriptionsRepository(), new MovieCatalogueFacade(new InMemoryRentalHistory(), movieRepository));
    }
}
