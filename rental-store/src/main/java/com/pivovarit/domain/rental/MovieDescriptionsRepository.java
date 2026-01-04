package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;

import java.util.Optional;

interface MovieDescriptionsRepository {
    Optional<MovieDescription> findByMovieId(MovieId movieId);

    MovieDescription save(MovieId movieId, String description);

    MovieDescription update(MovieId movieId, String description);

    boolean exists(MovieId movieId);
}
