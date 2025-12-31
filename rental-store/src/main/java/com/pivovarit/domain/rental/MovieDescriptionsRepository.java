package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;

import java.util.Optional;

public interface MovieDescriptionsRepository {
    Optional<MovieDescription> findByMovieId(MovieId movieId);

    void save(MovieId movieId, String description);

    void update(MovieId movieId, String description);
}
