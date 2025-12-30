package com.pivovarit.moviedescriptions.repository;

import com.pivovarit.moviedescriptions.model.entity.MovieDescription;
import com.pivovarit.moviedescriptions.model.entity.MovieId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InMemoryMovieDescriptionsRepository implements MovieDescriptionsRepository{

    private static List<MovieDescription> movieDescriptions =
            new ArrayList<>(List.of(
                    new MovieDescription(new MovieId(0), "Test"),
                    new MovieDescription(new MovieId(1), "Test2"),
                    new MovieDescription(new MovieId(2), "test3")
            ));

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return movieDescriptions.stream().filter(movieDescription -> movieDescription.id().id() == movieId.id()).findAny();
    }

    @Override
    public MovieId save(MovieDescription movieDescription) {
        this.deleteById(movieDescription.id());
        movieDescriptions.add(movieDescription);
        return movieDescription.id();
    }

    @Override
    public void deleteById(MovieId movieId) {
        movieDescriptions.removeIf(md -> md.id().equals(movieId));
    }

    @Override
    public Collection<MovieDescription> findAll() {
        return movieDescriptions;
    }
}
