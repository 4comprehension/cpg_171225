package com.pivovarit.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@WireMockTest
class RentalStoreClientTest {

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
      .options(wireMockConfig().dynamicPort().dynamicHttpsPort().notifier(new Slf4jNotifier(true)))
      .configureStaticDsl(true)
      .build();

    @Test
    void shouldFindAllMovies() throws Exception {
        stubMovies();

        RentalStoreClient client = new RentalStoreClient(wiremock.baseUrl(), RestClient.builder());

        System.out.println("client.findAllMovies() = " + client.findAllMovies());
    }

    @Test
    void shouldFindAllMoviesByType() throws Exception {
        stubMoviesByType("OLD");

        RentalStoreClient client = new RentalStoreClient(wiremock.baseUrl(), RestClient.builder());

        System.out.println("client.findAllMoviesByType('OLD') = " + client.findAllMoviesByType("OLD"));
    }

    @Disabled // TODO
    @Test
    void shouldSaveMovie() {
        RentalStoreClient client = new RentalStoreClient(wiremock.baseUrl(), RestClient.builder());

        WireMock.stubFor(WireMock.post(WireMock.urlPathEqualTo("/movies"))
          .willReturn(WireMock.aResponse()
            .withStatus(200)));

        client.saveMovie(new MovieAddRequest(42, "Foo", "NEW"));

        WireMock.verify(1, WireMock.putRequestedFor(WireMock.urlEqualTo("/movies/42")));
    }

    private static StubMapping stubMoviesByType(String type) {
        return WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/movies"))
          .withQueryParam("type", WireMock.matching(type))
          .willReturn(WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
              [
                {
                  "id": 1,
                  "title": "Avengers: Doomsday",
                  "type": "%s",
                  "description": "foo"
                },
                {
                  "id": 2,
                  "title": "Spiderman",
                  "type": "%s",
                  "description": "foo"
                }
              ]
              """.formatted(type, type))));
    }

    private static StubMapping stubMovies() {
        return WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/movies"))
          .willReturn(WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
              [
                {
                  "id": 1,
                  "title": "Avengers: Doomsday",
                  "type": "NEW",
                  "description": "foo"
                },
                {
                  "id": 2,
                  "title": "Spiderman",
                  "type": "NEW",
                  "description": "foo"
                },
                {
                  "id": 3,
                  "title": "Casablanca",
                  "type": "OLD",
                  "description": "foo"
                }
              ]
              """)));
    }
}
