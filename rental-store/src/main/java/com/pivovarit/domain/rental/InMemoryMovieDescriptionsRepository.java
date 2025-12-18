package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;

import java.util.Optional;

class InMemoryMovieDescriptionsRepository implements MovieDescriptionsRepository {
    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return Optional.of(new MovieDescription("foo"));
    }
}
