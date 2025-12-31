package com.pivovarit.moviedescriptions;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.pivovarit.moviedescriptions.client.MovieDescriptionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;


import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MovieDescriptionsIntegrationTest {

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
      .options(wireMockConfig().dynamicPort().dynamicHttpsPort().notifier(new Slf4jNotifier(true)))
      .configureStaticDsl(true)
      .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("service.rental-store.url", () -> wiremock.baseUrl());
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    void resetWireMock() {
        wiremock.resetAll();
    }

    @Test
    void shouldValidateMovieExistsBeforeAddingDescription() {
        // Arrange: movie exists in rental-store
        long movieId = 42L;
        stubMovieExists(movieId);

        // Act: add description
        var dto = new MovieDescriptionDto(movieId, "Great movie!");
        var addResponse = restTemplate.postForEntity("/movie-descriptions", dto, Void.class);

        // Assert: description was saved
        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Verify rental-store was called to validate movie
        wiremock.verify(1, getRequestedFor(urlPathEqualTo("/movies/" + movieId)));
    }

    @Test
    void shouldRejectDescriptionIfMovieDoesNotExist() {
        // Arrange: movie does NOT exist
        long movieId = 999L;
        stubMovieNotFound(movieId);

        // Act & Assert: should get 400 bad request
        var dto = new MovieDescriptionDto(movieId, "Non-existent movie description");
        var response = restTemplate.postForEntity("/movie-descriptions", dto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        // Verify rental-store was called
        wiremock.verify(1, getRequestedFor(urlPathEqualTo("/movies/" + movieId)));
    }

    @Test
    void shouldPersistDescriptionInH2Database() {
        long movieId = 42L;
        stubMovieExists(movieId);

        // Add description
        var dto = new MovieDescriptionDto(movieId, "Stored in H2");
        restTemplate.postForEntity("/movie-descriptions", dto, Void.class);

        // Retrieve and verify persistence
        var response = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().description()).isEqualTo("Stored in H2");
    }

    @Test
    void shouldUpdateExistingDescriptionInDatabase() {
        long movieId = 42L;
        stubMovieExists(movieId);

        // Add initial description
        var initial = new MovieDescriptionDto(movieId, "Initial description");
        restTemplate.postForEntity("/movie-descriptions", initial, Void.class);

        // Update with new description
        var updated = new MovieDescriptionDto(movieId, "Updated description");
        restTemplate.put("/movie-descriptions/{movieId}", updated, movieId);

        // Retrieve and verify update was persisted
        var response = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().description()).isEqualTo("Updated description");
    }

    @Test
    void shouldDeleteDescriptionFromDatabase() {
        long movieId = 42L;
        stubMovieExists(movieId);

        // Add description
        var dto = new MovieDescriptionDto(movieId, "To be deleted");
        restTemplate.postForEntity("/movie-descriptions", dto, Void.class);

        // Delete it
        restTemplate.delete("/movie-descriptions/{id}", movieId);

        // Verify it's gone
        var response = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturn404WhenDescriptionDoesNotExist() {
        long movieId = 99L;
        stubMovieNotFound(movieId);

        var resp = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private static void stubMovieExists(long movieId) {
        wiremock.stubFor(get(urlPathEqualTo("/movies/" + movieId))
          .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
              {
                "id": %d,
                "title": "Test Movie",
                "type": "DRAMA",
                "description": "Some description"
              }
              """.formatted(movieId))));
    }

    private static void stubMovieNotFound(long movieId) {
        wiremock.stubFor(get(urlPathEqualTo("/movies/" + movieId))
          .willReturn(aResponse()
            .withStatus(404)));
    }
}

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MovieDescriptionsIntegrationTest {

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
      .options(wireMockConfig().dynamicPort().dynamicHttpsPort().notifier(new Slf4jNotifier(true)))
      .configureStaticDsl(true)
      .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("service.rental-store.url", () -> wiremock.baseUrl());
    }

    @Autowired
    private TestRestTemplate restTemplate;

    
    @AfterEach
    void resetWireMock() {
        wiremock.resetAll();
    }

    @Test
    void shouldStoreAndRetrieveDescription() {
        long movieId = 42L;
        stubMovieExists(movieId);

        // Add description via controller
        var dto = new MovieDescriptionDto(movieId, "Some description");
        restTemplate.postForEntity("/movie-descriptions", dto, Void.class);

        // Retrieve and verify
        var resp = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        var body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.description()).isEqualTo("Some description");
        System.out.println("description retrieved: " + body.description());
    }

    @Test
    void shouldReturn404WhenDescriptionDoesNotExist() {
        long movieId = 99L;
        stubMovieNotFound(movieId);

        var resp = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void shouldUpdateExistingDescription() {
        long movieId = 42L;
        stubMovieExists(movieId);

        var dto = new MovieDescriptionDto(movieId, "Initial description");
        restTemplate.postForEntity("/movie-descriptions", dto, Void.class);

        var updatedDto = new MovieDescriptionDto(movieId, "Updated description");
        restTemplate.put("/movie-descriptions/{movieId}", updatedDto, movieId);

        var resp = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody().description()).isEqualTo("Updated description");
    }

    @Test
    void shouldDeleteDescription() {
        long movieId = 42L;
        stubMovieExists(movieId);

        var dto = new MovieDescriptionDto(movieId, "Description to delete");
        restTemplate.postForEntity("/movie-descriptions", dto, Void.class);

        restTemplate.delete("/movie-descriptions/{id}", movieId);

        var resp = restTemplate.getForEntity("/movie-descriptions/{id}", MovieDescriptionDto.class, movieId);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    private static void stubMovieExists(long movieId) {
        wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/movies/" + movieId))
          .willReturn(WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
              {
                "id": %d,
                "title": "Some Movie",
                "type": "REGULAR",
                "description": "Some description"
              }
              """.formatted(movieId))));
    }

    private static void stubMovieNotFound(long movieId) {
        wiremock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/movies/" + movieId))
          .willReturn(WireMock.aResponse()
            .withStatus(404)));
    }
}
