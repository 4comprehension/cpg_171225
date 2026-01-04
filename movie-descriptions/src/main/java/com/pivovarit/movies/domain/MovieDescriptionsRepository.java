package com.pivovarit.movies.domain;

import java.util.Optional;

public interface MovieDescriptionsRepository {
    Optional<String> findDescriptionByMovieId(long movieId);
    void save(long movieId, String description);
}
