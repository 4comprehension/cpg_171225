package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryMovieRepository {

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

    public void clean() {
        movies.clear();
    }
}
