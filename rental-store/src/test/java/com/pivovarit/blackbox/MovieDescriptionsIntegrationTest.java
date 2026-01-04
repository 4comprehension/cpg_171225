package com.pivovarit.blackbox;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.Matchers;
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

import static io.restassured.RestAssured.given;

@Testcontainers
class MovieDescriptionsIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(MovieDescriptionsIntegrationTest.class);
    private static final int WIREMOCK_PORT = 8080;
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
    static final WireMockContainer wiremock = new WireMockContainer()
            .withNetwork(network)
            .withNetworkAliases("wiremock")
            .withExposedPorts(WIREMOCK_PORT)
            .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("wiremock"))
            .waitingFor(Wait.forHttp("/__admin/health").forStatusCode(200));

    @Container
    static final ApplicationContainer app = new ApplicationContainer()
            .dependsOn(postgres, wiremock)
            .withNetwork(network)
            .withEnv("APPLICATION_PROFILE", "prod")
            .withEnv("POSTGRES_URL", "jdbc:postgresql://postgres:5432/postgres")
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "password")
            .withEnv("MOVIE_SERVICE_URL", "http://wiremock:8080") // wskazanie WireMock jako movie-descriptions
            .withExposedPorts(8080)
            .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("rental-store"))
            .waitingFor(Wait.forHttp("/health").forStatusCode(200));

    @Test
    void shouldFetchMovieDescriptionFromMovieDescriptionsService() {
        WireMock client = wiremock.newClient();

        // stub dla movie-descriptions
        client.register(WireMock.get("/movies/1/description")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"movieId\":1,\"description\":\"Legendary sci-fi movie\"}")));

        // dodanie filmu w rental-store
        given()
                .port(app.getMappedPort(8080))
                .contentType("application/json")
                .body("{\"id\":1,\"title\":\"Inception\",\"type\":\"REGULAR\"}")
                .post("/movies")
                .then()
                .statusCode(200);

        // sprawdzenie, czy rental-store pobiera opis z WireMock
        given()
                .port(app.getMappedPort(8080))
                .when()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(1))
                .body("[0].title", Matchers.equalTo("Inception"))
                .body("[0].description", Matchers.equalTo("Legendary sci-fi movie"));
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
