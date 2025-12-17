package com.pivovarit.rental.exception;

import com.pivovarit.rental.model.MovieId;

public class MovieAlreadyRentedException extends RuntimeException {

    public MovieAlreadyRentedException(MovieId movieId) {
        super("movie with id: " + movieId.id() + " is already rented");
    }
}
