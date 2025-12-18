package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;

final class MovieConverters {

    private MovieConverters() {
    }

    public static Movie from(MovieAddRequest movie) {
        return new Movie(new MovieId(movie.id()), movie.title(), MovieType.valueOf(movie.type()));
    }

    public static MovieDto from(Movie movie, String movieDescriptions) {
        return new MovieDto(movie.getId().id(), movie.getTitle(), movie.getType().toString(), movieDescriptions);
    }
}
