package com.pivovarit.domain.rental.exception;

import com.pivovarit.domain.rental.api.MovieId;

public class MovieDescriptionAlreadyExistsException extends RuntimeException {

    public MovieDescriptionAlreadyExistsException(MovieId movieId) {
        super("description for movie with id: " + movieId.id() + " already exists");
    }
}
