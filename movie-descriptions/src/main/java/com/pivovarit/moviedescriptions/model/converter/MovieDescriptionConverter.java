package com.pivovarit.moviedescriptions.model.converter;

import com.pivovarit.moviedescriptions.model.dto.request.CreateMovieDescriptionDTO;
import com.pivovarit.moviedescriptions.model.dto.response.MovieDescriptionDTO;
import com.pivovarit.moviedescriptions.model.entity.MovieDescription;
import com.pivovarit.moviedescriptions.model.entity.MovieId;

public final class MovieDescriptionConverter {

    private MovieDescriptionConverter() {
    }

    public static MovieDescription from(CreateMovieDescriptionDTO movie) {
        return new MovieDescription(new MovieId(movie.id()), movie.description());
    }

    public static MovieDescriptionDTO from(MovieDescription movieDescription) {
        return new MovieDescriptionDTO(movieDescription.id().id(), movieDescription.description());
    }
}
