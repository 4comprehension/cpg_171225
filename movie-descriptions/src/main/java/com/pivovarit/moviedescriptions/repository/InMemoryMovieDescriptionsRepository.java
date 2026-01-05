package com.pivovarit.moviedescriptions.repository;

import com.pivovarit.moviedescriptions.model.entity.MovieDescription;
import com.pivovarit.moviedescriptions.model.entity.MovieId;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMovieDescriptionsRepository implements MovieDescriptionsRepository{

    private final Map<MovieId, MovieDescription> movieDescriptions = new ConcurrentHashMap<>();

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return Optional.ofNullable(movieDescriptions.get(movieId));
    }

    @Override
    public MovieId save(MovieDescription movieDescription) {
        movieDescriptions.put(movieDescription.id(), movieDescription);
        return movieDescription.id();
    }

    @Override
    public void deleteById(MovieId movieId) {
        movieDescriptions.remove(movieId);
    }

    @Override
    public Collection<MovieDescription> findAll() {
        return List.copyOf(movieDescriptions.values());
    }

    public void clean() {
        movieDescriptions.clear();
    }
}
