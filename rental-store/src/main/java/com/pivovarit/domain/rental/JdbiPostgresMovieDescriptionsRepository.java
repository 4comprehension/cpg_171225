package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

class JdbiPostgresMovieDescriptionsRepository implements MovieDescriptionsRepository {

    private final Jdbi jdbi;

    JdbiPostgresMovieDescriptionsRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource).installPlugin(new PostgresPlugin());
    }

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId id) {
        return jdbi.withHandle(h -> h.createQuery("SELECT description FROM movie_descriptions WHERE movie_id = :movie_id")
            .bind("movie_id", id.id())
            .map((rs, _) -> new MovieDescription(rs.getString("description"))).findOne());
    }

    @Override
    public MovieDescription save(MovieId movieId, String description) {
        return jdbi.withHandle(h -> h.createQuery("""
            INSERT INTO movie_descriptions (description, movie_id) VALUES (:description, :movie_id) RETURNING description
            """)
            .bind("description", description)
            .bind("movie_id", movieId.id())
            .map((rs, _) -> new MovieDescription(rs.getString("description"))).one());
    }

    @Override
    public MovieDescription update(MovieId movieId, String description) {
        return jdbi.withHandle(h -> h.createQuery("""
            UPDATE movie_descriptions SET description = :description WHERE movie_id = :movie_id RETURNING description
            """)
            .bind("description", description)
            .bind("movie_id", movieId.id())
            .map((rs, _) -> new MovieDescription(rs.getString("description"))).one());
    }

    @Override
    public boolean exists(MovieId movieId) {
        return jdbi.withHandle(h -> h.createQuery("""
            SELECT EXISTS (SELECT 1 from movie_descriptions WHERE movie_id = :movie_id)
            """)
            .bind("movie_id", movieId.id())
            .mapTo(Boolean.class).one());
    }
}
