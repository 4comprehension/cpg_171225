package com.pivovarit.blackbox;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

@Testcontainers
class BlackboxTest {

    private static final int WIREMOCK_PORT = 8080;

    private static final Logger log = LoggerFactory.getLogger(BlackboxTest.class);

    private static final Network network = Network.newNetwork();

    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18")
      .withNetwork(network)
      .withNetworkAliases("postgres")
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("password")
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("postgres"))
      .waitingFor(Wait.forListeningPort());

    @Container
    static WireMockContainer wiremock = new WireMockContainer()
      .withNetwork(network)
      .withNetworkAliases("wiremock")
      .withExposedPorts(WIREMOCK_PORT)
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("wiremock"))
      .waitingFor(Wait.forHttp("/__admin/health").forStatusCode(200));

    @Container
    static ApplicationContainer app = new ApplicationContainer()
      .dependsOn(postgres, wiremock)
      .withNetwork(network)
      .withEnv("APPLICATION_PROFILE", "prod")
      .withEnv("POSTGRES_URL", "jdbc:postgresql://postgres:5432/postgres")
      .withEnv("POSTGRES_USER", "postgres")
      .withEnv("POSTGRES_PASSWORD", "password")
      .withEnv("MOVIE_DESCRIPTIONS_URL", "http://wiremock:8080")
      .withExposedPorts(8080)
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("rental-store"))
      .waitingFor(Wait.forHttp("/health").forStatusCode(200));

    @BeforeEach
    void setUp() {
        wiremock.newClient().resetMappings();
    }

    @Test
    void shouldFetchMovieDescriptionFromExternalService() {
        WireMock wireMockClient = wiremock.newClient();
        wireMockClient.register(get(urlPathEqualTo("/movie-descriptions/1"))
          .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
              {"movieId": 1, "description": "A great movie"}
              """)));

        given()
          .port(app.getMappedPort(8080))
          .contentType("application/json")
          .body("""
            {"id": 1,"title": "Test Movie","type": "NEW"}
            """)
          .post("/movies")
          .then()
          .statusCode(200);

        given()
          .port(app.getMappedPort(8080))
          .get("/movies/{id}", 1)
          .then()
          .statusCode(200)
          .body("id", Matchers.equalTo(1))
          .body("title", Matchers.equalTo("Test Movie"));

        wireMockClient.verifyThat(1, getRequestedFor(urlPathEqualTo("/movie-descriptions/1")));
    }

    @Test
    void shouldHandleMissingMovieDescription() {
        WireMock wireMockClient = wiremock.newClient();
        wireMockClient.register(get(urlPathEqualTo("/movie-descriptions/2"))
          .willReturn(aResponse().withStatus(404)));

        given()
          .port(app.getMappedPort(8080))
          .contentType("application/json")
          .body("""
            {"id": 2,"title": "No Description Movie","type": "REGULAR"}
            """)
          .post("/movies")
          .then()
          .statusCode(200);

        given()
          .port(app.getMappedPort(8080))
          .get("/movies/{id}", 2)
          .then()
          .statusCode(200)
          .body("id", Matchers.equalTo(2))
          .body("title", Matchers.equalTo("No Description Movie"));

        wireMockClient.verifyThat(1, getRequestedFor(urlPathEqualTo("/movie-descriptions/2")));
    }

    private static class ApplicationContainer extends GenericContainer<ApplicationContainer> {
        ApplicationContainer() {
            super(DockerImageName.parse("rental-store:snapshot"));
        }
    }

    private static class WireMockContainer extends GenericContainer<WireMockContainer> {
        WireMockContainer() {
            super(DockerImageName.parse("wiremock/wiremock:3x-alpine"));
        }

        public WireMock newClient() {
            return new WireMock(getHost(), getMappedPort(WIREMOCK_PORT));
        }
    }
}
