package com.pivovarit.domain.rental.exception;

import com.pivovarit.domain.rental.api.MovieId;

public class MovieDescriptionDoesNotExistException extends RuntimeException {

    public MovieDescriptionDoesNotExistException(MovieId movieId) {
        super("description for movie with id: " + movieId.id() + " does not exist");
    }
}
