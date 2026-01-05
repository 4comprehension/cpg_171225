package com.pivovarit.moviedescriptions.repository;


import com.pivovarit.moviedescriptions.model.entity.MovieDescription;
import com.pivovarit.moviedescriptions.model.entity.MovieId;

import java.util.Collection;
import java.util.Optional;

public interface MovieDescriptionsRepository {
    Optional<MovieDescription> findByMovieId(MovieId movieId);
    MovieId save(MovieDescription movieDescription);
    void deleteById(MovieId movieId);
    Collection<MovieDescription> findAll();
}
