package com.pivovarit.domain.rental.exception;

import com.pivovarit.domain.rental.api.MovieId;

public class CannotReturnException extends RuntimeException {

    public CannotReturnException(MovieId movieId) {
        super("can't return movie with id: " + movieId);
    }
}
