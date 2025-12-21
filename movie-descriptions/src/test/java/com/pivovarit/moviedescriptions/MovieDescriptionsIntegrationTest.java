package com.pivovarit.moviedescriptions;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pivovarit.moviedescriptions.dto.MovieDescriptionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "rentalstore.url=http://localhost:8082")
class MovieDescriptionsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8082);
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldFetchMovieDescriptionFromRentalStore() {
        // Arrange
        long movieId = 42L;
        wireMockServer.stubFor(get(urlPathEqualTo("/movies/42"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {"id": 42, "title": "Test Movie", "type": "NEW", "description": "Test Description"}
                    """)));

        // Act
        var response = restTemplate.getForObject("/movie-descriptions/{movieId}", MovieDescriptionDto.class, movieId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.movieId()).isEqualTo(42);
        assertThat(response.description()).isEqualTo("Test Description");
    }

    @Test
    void shouldReturnNotFoundWhenMovieDoesNotExist() {
        // Arrange
        long movieId = 999L;
        wireMockServer.stubFor(get(urlPathEqualTo("/movies/999"))
            .willReturn(aResponse().withStatus(404)));

        // Act & Assert
        var response = restTemplate.getForEntity("/movie-descriptions/{movieId}", String.class, movieId);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void shouldValidateMovieExistsBeforeAddingDescription() {
        // Arrange
        long movieId = 999L;
        wireMockServer.stubFor(get(urlPathEqualTo("/movies/999"))
            .willReturn(aResponse().withStatus(404)));

        // Act & Assert
        var response = restTemplate.postForEntity("/movie-descriptions", 
            new MovieDescriptionDto(movieId, "Some description"), 
            String.class);
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
    }
}
