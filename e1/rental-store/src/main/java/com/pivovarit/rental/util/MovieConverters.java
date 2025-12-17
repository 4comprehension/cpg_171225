package com.pivovarit.rental.util;

import com.pivovarit.rental.model.Movie;
import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.model.MovieDto;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.model.MovieType;

public final class MovieConverters {

    private MovieConverters() {
    }

    public static Movie from(MovieAddRequest movie) {
        return new Movie(new MovieId(movie.id()), movie.title(), MovieType.valueOf(movie.type()));
    }

    public static MovieDto from(Movie movie, String movieDescriptions) {
        return new MovieDto(movie.getId().id(), movie.getTitle(), movie.getType().toString(), movieDescriptions);
    }
}
