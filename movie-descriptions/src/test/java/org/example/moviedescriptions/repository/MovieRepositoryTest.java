package org.example.moviedescriptions.repository;

import org.example.moviedescriptions.model.Movie;
import org.example.moviedescriptions.testutil.MovieTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.moviedescriptions.testutil.MovieTestDataBuilder.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Movie Repository Tests")
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("Save and Find Operations")
    class SaveAndFindTests {

        @Test
        @DisplayName("Should save and find movie by id")
        void shouldSaveAndFindMovie() {
            // Given
            Movie movie = aMovie()
                    .withDescription("Test Description")
                    .build();

            // When
            Movie savedMovie = movieRepository.save(movie);
            entityManager.flush();
            entityManager.clear();
            
            Optional<Movie> foundMovie = movieRepository.findById(savedMovie.getMovieId());

            // Then
            assertThat(foundMovie).isPresent();
            assertThat(foundMovie.get().getMovieId()).isEqualTo(savedMovie.getMovieId());
            assertThat(foundMovie.get().getDescription()).isEqualTo("Test Description");
        }

        @Test
        @DisplayName("Should return empty when movie not found")
        void shouldReturnEmptyWhenMovieNotFound() {
            // When
            Optional<Movie> foundMovie = movieRepository.findById(999L);

            // Then
            assertThat(foundMovie).isEmpty();
        }

        @Test
        @DisplayName("Should check if movie exists by id")
        void shouldCheckIfMovieExistsById() {
            // Given
            Movie movie = aMovie()
                    .withDescription("Test Description")
                    .build();
            Movie savedMovie = movieRepository.save(movie);
            entityManager.flush();

            // When & Then
            assertThat(movieRepository.existsById(savedMovie.getMovieId())).isTrue();
            assertThat(movieRepository.existsById(999L)).isFalse();
        }
    }

    @Nested
    @DisplayName("Update Operations")
    class UpdateTests {

        @Test
        @DisplayName("Should update movie successfully")
        void shouldUpdateMovie() {
            // Given
            Movie movie = aMovie()
                    .withDescription("Original Description")
                    .build();
            Movie savedMovie = movieRepository.save(movie);
            entityManager.flush();
            entityManager.clear();

            // When
            savedMovie.setDescription("Updated Description");
            Movie updatedMovie = movieRepository.save(savedMovie);
            entityManager.flush();
            entityManager.clear();

            // Then
            Optional<Movie> foundMovie = movieRepository.findById(updatedMovie.getMovieId());
            assertThat(foundMovie).isPresent();
            assertThat(foundMovie.get().getDescription()).isEqualTo("Updated Description");
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteTests {

        @Test
        @DisplayName("Should delete movie successfully")
        void shouldDeleteMovie() {
            // Given
            Movie movie = aMovie()
                    .withDescription("Description")
                    .build();
            Movie savedMovie = movieRepository.save(movie);
            entityManager.flush();
            Long movieId = savedMovie.getMovieId();

            // When
            movieRepository.deleteById(movieId);
            entityManager.flush();
            entityManager.clear();

            // Then
            Optional<Movie> deletedMovie = movieRepository.findById(movieId);
            assertThat(deletedMovie).isEmpty();
        }

        @Test
        @DisplayName("Should handle delete of non-existent movie gracefully")
        void shouldHandleDeleteOfNonExistentMovie() {
            // When & Then - should not throw exception
            movieRepository.deleteById(999L);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should allow null description")
        void shouldAllowNullDescription() {
            // Given
            Movie movieWithNullDescription = aMovie()
                    .withNullDescription()
                    .build();

            // When
            Movie savedMovie = movieRepository.save(movieWithNullDescription);
            entityManager.flush();
            entityManager.clear();

            // Then
            Optional<Movie> foundMovie = movieRepository.findById(savedMovie.getMovieId());
            assertThat(foundMovie).isPresent();
            assertThat(foundMovie.get().getMovieId()).isEqualTo(savedMovie.getMovieId());
            assertThat(foundMovie.get().getDescription()).isNull();
        }

        @Test
        @DisplayName("Should handle long descriptions within limit")
        void shouldHandleLongDescriptionsWithinLimit() {
            // Given
            String longDescription = "A".repeat(2000); // Max allowed length
            Movie movie = aMovie()
                    .withDescription(longDescription)
                    .build();

            // When
            Movie savedMovie = movieRepository.save(movie);
            entityManager.flush();
            entityManager.clear();

            // Then
            Optional<Movie> foundMovie = movieRepository.findById(savedMovie.getMovieId());
            assertThat(foundMovie).isPresent();
            assertThat(foundMovie.get().getDescription()).hasSize(2000);
        }
    }

    @Nested
    @DisplayName("Custom Query Tests")
    class CustomQueryTests {

        @Test
        @DisplayName("Should find all movies")
        void shouldFindAllMovies() {
            // Given
            Movie movie1 = aMovie().withDescription("Description 1").build();
            Movie movie2 = aMovie().withDescription("Description 2").build();
            Movie movie3 = aMovie().withDescription("Description 3").build();
            
            movieRepository.save(movie1);
            movieRepository.save(movie2);
            movieRepository.save(movie3);
            entityManager.flush();

            // When
            var allMovies = movieRepository.findAll();

            // Then
            assertThat(allMovies).hasSize(3);
            assertThat(allMovies)
                    .extracting(Movie::getMovieId)
                    .containsExactlyInAnyOrder(movie1.getMovieId(), movie2.getMovieId(), movie3.getMovieId());
        }

        @Test
        @DisplayName("Should count movies correctly")
        void shouldCountMoviesCorrectly() {
            // Given
            Movie movie1 = aMovie().withDescription("Description 1").build();
            Movie movie2 = aMovie().withDescription("Description 2").build();
            
            movieRepository.save(movie1);
            movieRepository.save(movie2);
            entityManager.flush();

            // When
            long count = movieRepository.count();

            // Then
            assertThat(count).isEqualTo(2);
        }
    }
}