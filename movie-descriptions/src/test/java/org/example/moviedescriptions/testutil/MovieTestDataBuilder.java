package org.example.moviedescriptions.testutil;

import org.example.moviedescriptions.dto.MovieDescriptionDto;
import org.example.moviedescriptions.model.Movie;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Test Data Builder for Movie-related objects.
 * Provides fluent API for creating test data with sensible defaults.
 */
public class MovieTestDataBuilder {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public static class MovieBuilder {
        private Long movieId = ID_GENERATOR.getAndIncrement();
        private String description = "Default movie description";

        public MovieBuilder withMovieId(Long movieId) {
            this.movieId = movieId;
            return this;
        }

        public MovieBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public MovieBuilder withNullDescription() {
            this.description = null;
            return this;
        }

        public Movie build() {
            return new Movie(movieId, description);
        }
    }

    public static class MovieDescriptionDtoBuilder {
        private Long movieId = ID_GENERATOR.getAndIncrement();
        private String description = "Default movie description";

        public MovieDescriptionDtoBuilder withMovieId(Long movieId) {
            this.movieId = movieId;
            return this;
        }

        public MovieDescriptionDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public MovieDescriptionDtoBuilder withNullDescription() {
            this.description = null;
            return this;
        }

        public MovieDescriptionDto build() {
            return new MovieDescriptionDto(movieId, description);
        }
    }

    // Factory methods
    public static MovieBuilder aMovie() {
        return new MovieBuilder();
    }

    public static MovieDescriptionDtoBuilder aMovieDescriptionDto() {
        return new MovieDescriptionDtoBuilder();
    }

    // Predefined common test data
    public static class CommonMovies {
        public static final Movie THE_MATRIX = aMovie()
                .withMovieId(100L)
                .withDescription("A computer hacker learns about the true nature of reality.")
                .build();

        public static final Movie INCEPTION = aMovie()
                .withMovieId(200L)
                .withDescription("A thief who steals corporate secrets through dream-sharing technology.")
                .build();

        public static final Movie PULP_FICTION = aMovie()
                .withMovieId(300L)
                .withDescription("The lives of two mob hitmen, a boxer, and others intertwine.")
                .build();

        public static final MovieDescriptionDto THE_MATRIX_DTO = aMovieDescriptionDto()
                .withMovieId(101L)
                .withDescription("A computer hacker learns about the true nature of reality.")
                .build();

        public static final MovieDescriptionDto INCEPTION_DTO = aMovieDescriptionDto()
                .withMovieId(201L)
                .withDescription("A thief who steals corporate secrets through dream-sharing technology.")
                .build();

        public static final MovieDescriptionDto NULL_DESCRIPTION_DTO = aMovieDescriptionDto()
                .withMovieId(401L)
                .withNullDescription()
                .build();

        public static final MovieDescriptionDto LONG_DESCRIPTION_DTO = aMovieDescriptionDto()
                .withMovieId(501L)
                .withDescription("A".repeat(2000)) // Max allowed length
                .build();
    }
}