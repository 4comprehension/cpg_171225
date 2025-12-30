package com.pivovarit.moviedescriptions;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.pivovarit.moviedescriptions.client.RentalStoreClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class MovieDescriptionsIntegrationTest {

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
      .options(wireMockConfig().dynamicPort().dynamicHttpsPort().notifier(new Slf4jNotifier(true)))
      .configureStaticDsl(true)
      .build();

    @Test
    void shouldFetchMovieDescriptionFromRentalStore() {
        // Arrange
        long movieId = 42L;
        stubMovieExists(movieId);
        RentalStoreClient client = new RentalStoreClient(wiremock.baseUrl(), RestClient.builder());

        // Act
        boolean exists = client.movieExists(movieId);

        // Assert
        System.out.println("Movie exists result: " + exists);
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenMovieDoesNotExist() {
        // Arrange
        long movieId = 999L;
        stubMovieNotFound(movieId);
        RentalStoreClient client = new RentalStoreClient(wiremock.baseUrl(), RestClient.builder());

        // Act
        boolean exists = client.movieExists(movieId);

        // Assert
        System.out.println("Movie doesn't exists result: " + exists);
        assertThat(exists).isFalse();
    }

    @Test
    void shouldValidateMovieExistsBeforeAddingDescription() {
        // Arrange
        long movieId = 42L;
        stubMovieExists(movieId);
        RentalStoreClient client = new RentalStoreClient(wiremock.baseUrl(), RestClient.builder());

        // Act
        boolean exists = client.movieExists(movieId);

        // Assert
        System.out.println("Movie exists before adding descriptionresult: " + exists);
        assertThat(exists).isTrue();
    }

    private static StubMapping stubMovieExists(long movieId) {
        return WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/movies/" + movieId))
          .willReturn(WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
              {
                "id": %d,
                "title": "Test Movie",
                "type": "NEW",
                "description": "Test Description"
              }
              """.formatted(movieId))));
    }

    private static StubMapping stubMovieNotFound(long movieId) {
        return WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/movies/" + movieId))
          .willReturn(WireMock.aResponse()
            .withStatus(404)));
    }
}
