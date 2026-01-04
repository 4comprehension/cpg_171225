package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryMovieDescriptionsRepository implements MovieDescriptionsRepository {

    private final Map<Long, MovieDescription> data;

    public InMemoryMovieDescriptionsRepository() {
        this.data = new HashMap<>();
    }

    public InMemoryMovieDescriptionsRepository(Map<Long, MovieDescription> data) {
        this.data = data;
    }

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return Optional.of(data.get(movieId.id()));
    }

    @Override
    public MovieDescription save(MovieId movieId, String description) {
        data.put(movieId.id(), new MovieDescription(description));

        return data.get(movieId.id());
    }

    @Override
    public MovieDescription update(MovieId movieId, String description) {
        data.put(movieId.id(), new MovieDescription(description));

        return data.get(movieId.id());
    }

    @Override
    public boolean exists(MovieId movieId) {
        return data.containsKey(movieId.id());
    }
}
