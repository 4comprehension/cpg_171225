package org.example.moviedescriptions.service;

import org.example.moviedescriptions.dto.MovieDescriptionDto;
import org.example.moviedescriptions.model.Movie;
import org.example.moviedescriptions.repository.MovieRepository;
import org.example.moviedescriptions.testutil.MovieTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.moviedescriptions.testutil.MovieTestDataBuilder.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Movie Service Tests")
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Nested
    @DisplayName("Add Movie Operations")
    class AddMovieTests {

        @Test
        @DisplayName("Should add movie successfully")
        void shouldAddMovie() {
            // Given
            MovieDescriptionDto request = aMovieDescriptionDto()
                    .withDescription("The aging patriarch of an organized crime dynasty.")
                    .build();

            // When
            movieService.addMovie(request);

            // Then
            Optional<Movie> savedMovie = movieRepository.findById(request.movieId());
            assertThat(savedMovie).isPresent();
            assertThat(savedMovie.get().getMovieId()).isEqualTo(request.movieId());
            assertThat(savedMovie.get().getDescription()).isEqualTo("The aging patriarch of an organized crime dynasty.");
        }

        @Test
        @DisplayName("Should throw exception when movie already exists")
        void shouldThrowExceptionWhenMovieAlreadyExists() {
            // Given
            Movie existingMovie = aMovie()
                    .withDescription("Existing description")
                    .build();
            movieRepository.save(existingMovie);
            
            MovieDescriptionDto request = aMovieDescriptionDto()
                    .withMovieId(existingMovie.getMovieId())
                    .withDescription("New description")
                    .build();

            // When & Then
            assertThatThrownBy(() -> movieService.addMovie(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Movie with ID " + existingMovie.getMovieId() + " already exists.");
        }
    }

    @Nested
    @DisplayName("Get Movie Operations")
    class GetMovieTests {

        @Test
        @DisplayName("Should get movie description when movie exists")
        void shouldGetMovieDescription() {
            // Given
            Movie movie = aMovie()
                    .withDescription("The lives of two mob hitmen.")
                    .build();
            Movie savedMovie = movieRepository.save(movie);

            // When
            Optional<MovieDescriptionDto> response = movieService.getMovieDescription(savedMovie.getMovieId());

            // Then
            assertThat(response).isPresent();
            assertThat(response.get().movieId()).isEqualTo(savedMovie.getMovieId());
            assertThat(response.get().description()).isEqualTo("The lives of two mob hitmen.");
        }

        @Test
        @DisplayName("Should return empty when movie not found")
        void shouldReturnEmptyWhenMovieNotFound() {
            // When
            Optional<MovieDescriptionDto> response = movieService.getMovieDescription(999L);

            // Then
            assertThat(response).isEmpty();
        }
    }

    @Nested
    @DisplayName("Update Movie Operations")
    class UpdateMovieTests {

        @Test
        @DisplayName("Should update movie description when movie exists")
        void shouldUpdateMovieDescription() {
            // Given
            Movie movie = aMovie()
                    .withDescription("Original description")
                    .build();
            Movie savedMovie = movieRepository.save(movie);
            
            MovieDescriptionDto updateRequest = aMovieDescriptionDto()
                    .withMovieId(savedMovie.getMovieId())
                    .withDescription("New description")
                    .build();

            // When
            Optional<MovieDescriptionDto> response = movieService.updateMovieDescription(savedMovie.getMovieId(), updateRequest);

            // Then
            assertThat(response).isPresent();
            assertThat(response.get().movieId()).isEqualTo(savedMovie.getMovieId());
            assertThat(response.get().description()).isEqualTo("New description");
            
            // Verify persistence
            Optional<Movie> updatedMovie = movieRepository.findById(savedMovie.getMovieId());
            assertThat(updatedMovie).isPresent();
            assertThat(updatedMovie.get().getDescription()).isEqualTo("New description");
        }

        @Test
        @DisplayName("Should return empty when updating non-existent movie")
        void shouldReturnEmptyWhenUpdatingNonExistentMovie() {
            // Given
            MovieDescriptionDto updateRequest = aMovieDescriptionDto()
                    .withMovieId(999L)
                    .withDescription("Description")
                    .build();

            // When
            Optional<MovieDescriptionDto> response = movieService.updateMovieDescription(999L, updateRequest);

            // Then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("Should preserve movieId when updating")
        void shouldPreserveMovieIdWhenUpdating() {
            // Given
            Movie movie = aMovie()
                    .withDescription("Original description")
                    .build();
            Movie savedMovie = movieRepository.save(movie);
            
            MovieDescriptionDto updateRequest = aMovieDescriptionDto()
                    .withMovieId(999L) // Different ID in request
                    .withDescription("New description")
                    .build();

            // When
            Optional<MovieDescriptionDto> response = movieService.updateMovieDescription(savedMovie.getMovieId(), updateRequest);

            // Then
            assertThat(response).isPresent();
            assertThat(response.get().movieId()).isEqualTo(savedMovie.getMovieId()); // Should preserve original ID
            assertThat(response.get().description()).isEqualTo("New description");
        }
    }
}