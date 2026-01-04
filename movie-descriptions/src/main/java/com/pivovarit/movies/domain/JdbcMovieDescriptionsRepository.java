package com.pivovarit.movies.domain;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcMovieDescriptionsRepository implements MovieDescriptionsRepository {

    private final Jdbi jdbi;

    public JdbcMovieDescriptionsRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Optional<String> findDescriptionByMovieId(long movieId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT description
                FROM movie_descriptions
                WHERE movie_id = :movieId
                """)
                        .bind("movieId", movieId)
                        .mapTo(String.class)
                        .findOne()
        );
    }

    @Override
    public void save(long movieId, String description) {
        jdbi.useHandle(handle -> {
            int updated = handle.createUpdate("""
                UPDATE movie_descriptions
                SET description = :description
                WHERE movie_id = :movieId
                """)
                    .bind("movieId", movieId)
                    .bind("description", description)
                    .execute();

            if (updated == 0) {
                handle.createUpdate("""
                    INSERT INTO movie_descriptions(movie_id, description)
                    VALUES (:movieId, :description)
                    """)
                        .bind("movieId", movieId)
                        .bind("description", description)
                        .execute();
            }
        });
    }
}
