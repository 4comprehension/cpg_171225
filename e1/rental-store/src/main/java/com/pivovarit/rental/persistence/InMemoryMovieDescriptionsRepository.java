package com.pivovarit.rental.persistence;

import com.pivovarit.rental.model.MovieDescription;
import com.pivovarit.rental.model.MovieId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InMemoryMovieDescriptionsRepository {
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return Optional.of(new MovieDescription("foo"));
    }
}
