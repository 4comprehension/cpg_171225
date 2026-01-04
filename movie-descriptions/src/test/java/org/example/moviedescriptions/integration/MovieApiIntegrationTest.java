package org.example.moviedescriptions.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.moviedescriptions.dto.MovieDescriptionDto;
import org.example.moviedescriptions.model.Movie;
import org.example.moviedescriptions.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Movie API Integration Tests")
class MovieApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Nested
    @DisplayName("Complete Movie Lifecycle")
    class MovieLifecycleTests {

        @Test
        @DisplayName("Should handle complete movie lifecycle: create, read, update")
        void shouldHandleCompleteMovieLifecycle() throws Exception {
            // Create movie
            MovieDescriptionDto createRequest = new MovieDescriptionDto(1001L, "A computer hacker learns about reality.");
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated());

            // Read movie
            mockMvc.perform(get("/movies/{movieId}", 1001L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.movieId", is(1001)))
                    .andExpect(jsonPath("$.description", is("A computer hacker learns about reality.")));

            // Update movie
            MovieDescriptionDto updateRequest = new MovieDescriptionDto(1001L, "Neo continues his quest.");
            
            mockMvc.perform(put("/movies/{movieId}", 1001L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.movieId", is(1001)))
                    .andExpect(jsonPath("$.description", is("Neo continues his quest.")));

            // Verify update persisted
            mockMvc.perform(get("/movies/{movieId}", 1001L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.description", is("Neo continues his quest.")));
        }

        @Test
        @DisplayName("Should prevent duplicate movie creation")
        void shouldPreventDuplicateMovieCreation() throws Exception {
            // Create first movie
            MovieDescriptionDto createRequest = new MovieDescriptionDto(1002L, "Original description");
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated());

            // Try to create duplicate
            MovieDescriptionDto duplicateRequest = new MovieDescriptionDto(1002L, "Duplicate description");
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(duplicateRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle not found errors properly")
        void shouldHandleNotFoundErrors() throws Exception {
            // Try to get non-existent movie
            mockMvc.perform(get("/movies/{movieId}", 999L))
                    .andExpect(status().isNotFound());

            // Try to update non-existent movie
            MovieDescriptionDto updateRequest = new MovieDescriptionDto(999L, "Description");
            mockMvc.perform(put("/movies/{movieId}", 999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should handle malformed JSON")
        void shouldHandleMalformedJson() throws Exception {
            String malformedJson = "{\"movieId\": 1, \"description\":}"; // Invalid JSON

            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(malformedJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle missing content type")
        void shouldHandleMissingContentType() throws Exception {
            MovieDescriptionDto request = new MovieDescriptionDto(1L, "Description");

            mockMvc.perform(post("/movies")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }

    @Nested
    @DisplayName("Data Persistence")
    class DataPersistenceTests {

        @Test
        @DisplayName("Should persist data correctly across operations")
        void shouldPersistDataCorrectly() throws Exception {
            // Create movie via API
            MovieDescriptionDto request = new MovieDescriptionDto(1003L, "Dreams within dreams.");
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            // Verify data exists in repository
            Movie savedMovie = movieRepository.findById(1003L).orElseThrow();
            assert savedMovie.getMovieId().equals(1003L);
            assert savedMovie.getDescription().equals("Dreams within dreams.");
        }

        @Test
        @DisplayName("Should handle concurrent updates correctly")
        void shouldHandleConcurrentUpdatesCorrectly() throws Exception {
            // Create initial movie
            movieRepository.save(new Movie(1004L, "Original description"));

            // Perform multiple updates
            MovieDescriptionDto update1 = new MovieDescriptionDto(1004L, "First update");
            MovieDescriptionDto update2 = new MovieDescriptionDto(1004L, "Second update");

            mockMvc.perform(put("/movies/{movieId}", 1004L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(update1)))
                    .andExpect(status().isOk());

            mockMvc.perform(put("/movies/{movieId}", 1004L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(update2)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.description", is("Second update")));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null description")
        void shouldHandleNullDescription() throws Exception {
            MovieDescriptionDto request = new MovieDescriptionDto(1005L, null);
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/movies/{movieId}", 1005L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.movieId", is(1005)))
                    .andExpect(jsonPath("$.description").doesNotExist());
        }

        @Test
        @DisplayName("Should handle very long descriptions")
        void shouldHandleVeryLongDescriptions() throws Exception {
            String longDescription = "A".repeat(2000); // Max allowed length
            MovieDescriptionDto request = new MovieDescriptionDto(1006L, longDescription);
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/movies/{movieId}", 1006L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.description").value(longDescription));
        }

        @Test
        @DisplayName("Should handle zero and negative movie IDs")
        void shouldHandleZeroAndNegativeMovieIds() throws Exception {
            // Test with zero ID
            MovieDescriptionDto zeroIdRequest = new MovieDescriptionDto(0L, "Zero ID movie");
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(zeroIdRequest)))
                    .andExpect(status().isCreated());

            // Test with negative ID
            MovieDescriptionDto negativeIdRequest = new MovieDescriptionDto(-1L, "Negative ID movie");
            
            mockMvc.perform(post("/movies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(negativeIdRequest)))
                    .andExpect(status().isCreated());
        }
    }
}