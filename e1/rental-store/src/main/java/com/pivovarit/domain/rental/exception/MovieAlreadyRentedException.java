package com.pivovarit.domain.rental.exception;

import com.pivovarit.domain.rental.api.MovieId;

public class MovieAlreadyRentedException extends RuntimeException {

    public MovieAlreadyRentedException(MovieId movieId) {
        super("movie with id: " + movieId.id() + " is already rented");
    }
}
