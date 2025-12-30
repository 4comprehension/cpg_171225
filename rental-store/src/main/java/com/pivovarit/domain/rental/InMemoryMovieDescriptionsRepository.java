
package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMovieDescriptionsRepository implements MovieDescriptionsRepository {

    private final Map<Long, String> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return Optional.ofNullable(storage.get(movieId.id())).map(MovieDescription::new);
    }

    @Override
    public void save(MovieId movieId, String description) {
        storage.put(movieId.id(), description);
    }

    @Override
    public void update(MovieId movieId, String description) {
        storage.put(movieId.id(), description);
    }
}
