package org.example.moviedescriptions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.moviedescriptions.dto.MovieDescriptionDto;
import org.example.moviedescriptions.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@DisplayName("Movie Controller Tests")
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /movie-descriptions - Add Movie")
    class AddMovieTests {

        @Test
        @DisplayName("Should create new movie successfully")
        void shouldAddNewMovie() throws Exception {
            // Given
            MovieDescriptionDto request = new MovieDescriptionDto(1L, "A computer hacker learns about the true nature of reality.");
            
            doNothing().when(movieService).addMovie(any(MovieDescriptionDto.class));

            // When & Then
            mockMvc.perform(post("/movie-descriptions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle service exceptions")
        void shouldHandleServiceExceptions() throws Exception {
            // Given
            MovieDescriptionDto request = new MovieDescriptionDto(1L, "Description");
            
            doThrow(new IllegalArgumentException("Movie already exists"))
                    .when(movieService).addMovie(any(MovieDescriptionDto.class));

            // When & Then
            mockMvc.perform(post("/movie-descriptions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /movie-descriptions/{movieId} - Get Movie")
    class GetMovieTests {

        @Test
        @DisplayName("Should return movie when exists")
        void shouldGetMovieDescription() throws Exception {
            // Given
            MovieDescriptionDto response = new MovieDescriptionDto(1L, "A thief who steals corporate secrets through dream-sharing technology.");
            
            when(movieService.getMovieDescription(1L)).thenReturn(Optional.of(response));

            // When & Then
            mockMvc.perform(get("/movie-descriptions/{movieId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.movieId", is(1)))
                    .andExpect(jsonPath("$.description", is("A thief who steals corporate secrets through dream-sharing technology.")));
        }

        @Test
        @DisplayName("Should return not found for non-existent movie")
        void shouldReturnNotFoundForNonExistentMovie() throws Exception {
            // Given
            when(movieService.getMovieDescription(999L)).thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(get("/movie-descriptions/{movieId}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /movie-descriptions/{movieId} - Update Movie")
    class UpdateMovieTests {

        @Test
        @DisplayName("Should update movie when exists")
        void shouldUpdateMovieDescription() throws Exception {
            // Given
            MovieDescriptionDto updateRequest = new MovieDescriptionDto(1L, "Updated description");
            MovieDescriptionDto response = new MovieDescriptionDto(1L, "Updated description");
            
            when(movieService.updateMovieDescription(eq(1L), any(MovieDescriptionDto.class)))
                    .thenReturn(Optional.of(response));

            // When & Then
            mockMvc.perform(put("/movie-descriptions/{movieId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.movieId", is(1)))
                    .andExpect(jsonPath("$.description", is("Updated description")));
        }

        @Test
        @DisplayName("Should return not found when updating non-existent movie")
        void shouldReturnNotFoundWhenUpdatingNonExistentMovie() throws Exception {
            // Given
            MovieDescriptionDto updateRequest = new MovieDescriptionDto(999L, "Description");
            
            when(movieService.updateMovieDescription(eq(999L), any(MovieDescriptionDto.class)))
                    .thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(put("/movie-descriptions/{movieId}", 999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should handle malformed JSON")
        void shouldHandleMalformedJson() throws Exception {
            // Given
            String malformedJson = "{\"movieId\": 1, \"description\":}"; // Invalid JSON

            // When & Then
            mockMvc.perform(post("/movie-descriptions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(malformedJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle missing content type")
        void shouldHandleMissingContentType() throws Exception {
            // Given
            MovieDescriptionDto request = new MovieDescriptionDto(1L, "Description");

            // When & Then
            mockMvc.perform(post("/movie-descriptions")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }
}