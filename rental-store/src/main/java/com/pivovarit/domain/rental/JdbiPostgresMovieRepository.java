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

class JdbiPostgresMovieRepository implements MovieRepository {

    private final Jdbi jdbi;

    JdbiPostgresMovieRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource).installPlugin(new PostgresPlugin());
    }

    @Override
    public MovieId save(Movie movie) {
        jdbi.useHandle(h -> h.createUpdate("""
            INSERT INTO movies (id, title, type) VALUES (:id, :title, :type)
            """)
          .bind("id", movie.getId().id())
          .bind("title", movie.getTitle())
          .bind("type", movie.getType().toString())
          .execute()
        );

        return movie.getId();
    }

    @Override
    public Collection<Movie> findAll() {
        return jdbi.withHandle(h -> h.createQuery("SELECT * FROM movies").map((rs, _) -> asJdbiMovie(rs)).list());
    }

    @Override
    public Collection<Movie> findByType(String type) {
        return jdbi.withHandle(h -> h.createQuery("SELECT * FROM movies WHERE type = :type")
          .bind("type", type)
          .map((rs, _) -> asJdbiMovie(rs)).list());
    }

    private static Movie asJdbiMovie(ResultSet rs) throws SQLException {
        return new Movie(
          new MovieId(rs.getLong("id")),
          rs.getString("title"),
          MovieType.valueOf(rs.getString("type"))
        );
    }

    @Override
    public Optional<Movie> findById(MovieId id) {
        return jdbi.withHandle(h -> h.createQuery("SELECT * FROM movies WHERE id = :id")
          .bind("id", id.id())
          .map((rs, _) -> asJdbiMovie(rs)).findOne());
    }

    private static RowMapper<Movie> asMovie() {
        return (rs, _) -> asJdbiMovie(rs);
    }
}
