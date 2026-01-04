package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("dev")
public class InMemoryMovieDescriptionsRepository implements MovieDescriptionsRepository {

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        // prosty stub, zawsze zwraca "foo" — wystarczy do testowania integracji w dev
        return Optional.of(new MovieDescription("foo"));
    }
}
