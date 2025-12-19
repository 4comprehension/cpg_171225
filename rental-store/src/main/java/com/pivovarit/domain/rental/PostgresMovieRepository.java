package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.Collection;
import java.util.Optional;

class PostgresMovieRepository implements MovieRepository {

    private final JdbcClient jdbcClient;

    PostgresMovieRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public MovieId save(Movie movie) {
        jdbcClient.sql("""
            INSERT INTO movies (id, title, type) VALUES (?, ?, ?)
            """)
          .param(movie.getId().id())
          .param(movie.getTitle())
          .param(movie.getType().toString())
          .update();

        return movie.getId();
    }

    @Override
    public Collection<Movie> findAll() {
        return jdbcClient.sql("SELECT * FROM movies").query(asMovie()).list();
    }

    @Override
    public Collection<Movie> findByType(String type) {
        return jdbcClient.sql("SELECT * FROM movies WHERE type = ?")
          .param(type)
          .query(asMovie()).list();
    }

    @Override
    public Optional<Movie> findById(MovieId id) {
        return jdbcClient.sql("SELECT * FROM movies WHERE id = ?")
          .param(id.id())
          .query(asMovie())
          .optional();
    }

    private static RowMapper<Movie> asMovie() {
        return (rs, _) -> new Movie(
          new MovieId(rs.getLong("id")),
          rs.getString("title"),
          MovieType.valueOf(rs.getString("type"))
        );
    }
}
