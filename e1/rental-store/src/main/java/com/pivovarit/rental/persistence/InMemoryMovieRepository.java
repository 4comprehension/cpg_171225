package com.pivovarit.rental.persistence;

import com.pivovarit.rental.model.Movie;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.model.MovieType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryMovieRepository {

    private final Map<MovieId, Movie> movies = new ConcurrentHashMap<>();

    public MovieId save(Movie movie) {
        movies.put(movie.getId(), movie);
        return movie.getId();
    }

    public Collection<Movie> findAll() {
        return movies.values();
    }

    public Collection<Movie> findByType(String type) {
        return movies.values().stream().filter(i -> i.getType().equals(MovieType.valueOf(type))).toList();
    }

    public Optional<Movie> findById(MovieId id) {
        return Optional.ofNullable(movies.get(id));
    }
}
