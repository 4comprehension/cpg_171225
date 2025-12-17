package com.pivovarit.rental.persistence;

import com.pivovarit.rental.model.MovieDescription;
import com.pivovarit.rental.model.MovieId;

import java.util.Optional;

public class InMemoryMovieDescriptionsRepository {
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return Optional.of(new MovieDescription("foo"));
    }
}
