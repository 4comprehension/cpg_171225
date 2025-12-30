package com.pivovarit.moviedescriptions;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.pivovarit.moviedescriptions.client.MovieDescriptionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    void shouldStoreAndRetrieveDescriptionUsingH2() {
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
                "type": "DRAMA",
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
