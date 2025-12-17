package com.pivovarit.rental.exception;

import com.pivovarit.rental.model.MovieId;

public class CannotReturnException extends RuntimeException {

    public CannotReturnException(MovieId movieId) {
        super("can't return movie with id: " + movieId);
    }
}
