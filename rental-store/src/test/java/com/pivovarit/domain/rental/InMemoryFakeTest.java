package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDescriptionDto;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

    @Test
    void shouldReturnMovieDescription() {
        long id = 1111;
        String description = "Movie description";

        assertThat(rentalFacade.findDescriptionByMovieId(new MovieId(id)))
            .map(MovieDescriptionDto::description)
            .contains(description);
    }

    @Test
    void shouldAddMovieDescription() {
        long id = 3333;
        String description = "Movie description to be added";

        rentalFacade.addDescriptionByMovieId(new MovieId(id), description);

        assertThat(rentalFacade.findDescriptionByMovieId(new MovieId(id)))
            .map(MovieDescriptionDto::description)
            .contains(description);
    }

    @Test
    void shouldUpdateMovieDescription() {
        long id = 2222L;

        String description = "Second movie description";
        String descriptionToBeUpdated = "Movie description to be updated";

        assertThat(rentalFacade.findDescriptionByMovieId(new MovieId(id)))
            .map(MovieDescriptionDto::description)
            .contains(description);

        rentalFacade.updateDescriptionByMovieId(new MovieId(id), descriptionToBeUpdated);

        assertThat(rentalFacade.findDescriptionByMovieId(new MovieId(id)))
            .map(MovieDescriptionDto::description)
            .contains(descriptionToBeUpdated);
    }

    private static RentalFacade inMemoryInstance() {
        Map<Long, MovieDescription> data = LongStream.rangeClosed(1, 1000)
            .boxed()
            .collect(Collectors.toMap(Function.identity(), _ -> new MovieDescription("foo")));

        data.put(1111L, new MovieDescription("Movie description"));
        data.put(2222L, new MovieDescription("Second movie description"));

        InMemoryMovieRepository movieRepository = new InMemoryMovieRepository();
        return new RentalFacade(movieRepository, new InMemoryMovieDescriptionsRepository(data), new MovieRentalService(new InMemoryRentalHistory(), movieRepository));
    }
}
