package com.pivovarit.moviedescriptions.repository;

import com.pivovarit.moviedescriptions.model.entity.MovieDescription;
import com.pivovarit.moviedescriptions.model.entity.MovieId;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbiH2MovieDescriptionsRepository implements MovieDescriptionsRepository{


    private final Jdbi jdbi;

    public JdbiH2MovieDescriptionsRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return jdbi.withHandle(h -> h.createQuery("SELECT * FROM movie_descriptions WHERE id = :movieid")
                .bind("movieid",movieId.id())
                .map((rs, _) -> asJdbiMovieDescription(rs))
                .findOne());
    }

    @Override
    public MovieId save(MovieDescription movieDescription) {
        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO movie_descriptions (id, description) VALUES (:id,:description)")
                .bind("id",movieDescription.id().id())
                .bind("description",movieDescription.description())
                .execute()
        );
        return movieDescription.id();
    }

    @Override
    public void deleteById(MovieId movieId) {
        jdbi.withHandle(handle -> handle.execute("DELETE FROM movie_descriptions WHERE id=(?)",movieId.id()));
    }

    @Override
    public Collection<MovieDescription> findAll() {
        return jdbi.withHandle(h -> h.createQuery("SELECT * FROM movie_descriptions").map((rs, _) -> asJdbiMovieDescription(rs)).list());
    }


    private static MovieDescription asJdbiMovieDescription(ResultSet rs) throws SQLException {
        return new MovieDescription(
                new MovieId(rs.getLong("id")),
                rs.getString("description")
        );
    }
}
