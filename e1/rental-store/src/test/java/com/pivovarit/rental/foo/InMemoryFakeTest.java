package com.pivovarit.rental.foo;

import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.model.MovieDto;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.persistence.InMemoryMovieDescriptionsRepository;
import com.pivovarit.rental.persistence.InMemoryMovieRepository;
import com.pivovarit.rental.persistence.InMemoryRentalHistory;
import com.pivovarit.rental.service.MovieRentalService;
import com.pivovarit.rental.service.RentalService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class InMemoryFakeTest {

    @RepeatedTest(100_000)
    void shouldAddMovie() {
        var rentalService = inMemoryInstance();
        MovieAddRequest movieAddRequest = new MovieAddRequest(42, "Avengers", "NEW");

        rentalService.addMovie(movieAddRequest);
        Assertions.assertThat(rentalService.findMovieById(new MovieId(42)))
          .map(MovieDto::id)
          .contains(42L);
    }

    private static RentalService inMemoryInstance() {
        InMemoryMovieRepository movieRepository = new InMemoryMovieRepository();
        return new RentalService(movieRepository, new InMemoryMovieDescriptionsRepository(), new MovieRentalService(new InMemoryRentalHistory(), movieRepository));
    }
}
