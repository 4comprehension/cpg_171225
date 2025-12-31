package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class JdbcMovieDescriptionsRepository implements MovieDescriptionsRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMovieDescriptionsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        try {
            String desc = jdbcTemplate.queryForObject(
                "SELECT description FROM movie_descriptions WHERE movie_id = ?",
                String.class,
                movieId.id());
            return Optional.of(new MovieDescription(desc));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(MovieId movieId, String description) {
        int updated = jdbcTemplate.update(
            "UPDATE movie_descriptions SET description = ? WHERE movie_id = ?",
            description, movieId.id());
        if (updated == 0) {
            jdbcTemplate.update(
                "INSERT INTO movie_descriptions (movie_id, description) VALUES (?, ?)",
                movieId.id(), description);
        }
    }

    @Override
    public void update(MovieId movieId, String description) {
        jdbcTemplate.update(
            "UPDATE movie_descriptions SET description = ? WHERE movie_id = ?",
            description, movieId.id());
    }
}
