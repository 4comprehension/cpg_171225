package com.pivovarit.domain.rental.exception;

import com.pivovarit.domain.rental.api.MovieId;

public class MovieNotExistsException extends RuntimeException {

    public MovieNotExistsException(MovieId movieId) {
        super("movie with id: " + movieId.id() + " does not exist");
    }
}
