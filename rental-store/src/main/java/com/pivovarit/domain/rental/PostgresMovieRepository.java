package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

class PostgresMovieRepository implements MovieRepository {

    private final JdbcClient jdbcClient;

    PostgresMovieRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public MovieId save(Movie movie) {
        return null;
    }

    @Override
    public Collection<Movie> findAll() {
        return List.of();
    }

    @Override
    public Collection<Movie> findByType(String type) {
        return List.of();
    }

    @Override
    public Optional<Movie> findById(MovieId id) {
        return Optional.empty();
    }
}
