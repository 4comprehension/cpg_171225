package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;

import java.util.Optional;

public class InMemoryMovieDescriptionsRepository implements MovieDescriptionsRepository {
    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return Optional.of(new MovieDescription("foo"));
    }

    @Override
    public void save(MovieId movieId, String description) {
        // noop for demo
    }

    @Override
    public void update(MovieId movieId, String description) {
        // noop for demo
    }
}
