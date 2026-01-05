package com.pivovarit.blackbox;

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

import com.github.tomakehurst.wiremock.client.WireMock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

import static io.restassured.RestAssured.given;

@Testcontainers
class BlackboxTest {

    private static final int WIREMOCK_PORT = 8080;

    private static final Logger log = LoggerFactory.getLogger(BlackboxTest.class);

    private static final Network network = Network.newNetwork();

    @Container
    static final PostgresTestContainer postgres = new PostgresTestContainer("postgres:18");

    static {
        postgres.withNetwork(network);
        postgres.withNetworkAliases("postgres");
        postgres.withDatabaseName("postgres");
        postgres.withUsername("postgres");
        postgres.withPassword("password");
        postgres.withLogConsumer(new Slf4jLogConsumer(log).withPrefix("postgres"));
        postgres.waitingFor(Wait.forListeningPort());
    }

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
            .withEnv(
                    "SERVICE_MOVIE_DESCRIPTION_URL",
                    "http://wiremock:8080"
            )
            .withExposedPorts(8080)
            .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("rental-store"))
            .waitingFor(Wait.forHttp("/health").forStatusCode(200));

    @BeforeEach
    void setupWireMockStubs() {
        postgres.truncateAll();
        WireMock wireMock = wiremock.newClient();
        wireMock.register(get(urlPathMatching("/movie-descriptions/.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {
                            "id":"42",
                            "description": "test description"
                        }
                        """)
                        .withStatus(200)));
    }

    @Test
    void shouldCreateMovie() {
        given()
                .port(app.getMappedPort(8080))
                .when()
                .get("/movies")
                .then()
                .body("", Matchers.hasSize(0))
                .statusCode(200);

        given()
                .port(app.getMappedPort(8080))
                .when()
                .contentType("application/json")
                .body("""
            {"id": 42,"title": "Foo","type": "NEW"}
            """)
                .post("/movies")
                .then()
                .statusCode(200);

        given()
                .port(app.getMappedPort(8080))
                .when()
                .get("/movies")
                .then()
                .body("", Matchers.hasSize(1))
                .body("find { it.title == 'Foo' }.id", Matchers.equalTo(42))
                .body("find { it.title == 'Foo' }.type", Matchers.equalTo("NEW"))
                .statusCode(200);

        given()
                .port(app.getMappedPort(8080))
                .when()
                .get("/movies/{id}", 42)
                .then()
                .body("id", Matchers.equalTo(42))
                .body("title", Matchers.equalTo("Foo"))
                .body("type", Matchers.equalTo("NEW"))
                .statusCode(200);
    }

    @Test
    void shouldRejectMovieWithNegativeId() {
        given()
                .port(app.getMappedPort(8080))
                .when()
                .contentType("application/json")
                .body("""
            {"id": -42,"title": "Foo","type": "NEW"}
            """)
                .post("/movies")
                .then()
                .statusCode(400)
                .body("id", Matchers.is("must be greater than 0"));
    }

    @Test
    void shouldRejectMovieWithTooLongTitle() {
        given()
                .port(app.getMappedPort(8080))
                .when()
                .contentType("application/json")
                .body("""
            {"id": 42,"title": "%s","type": "NEW"}
            """.formatted("a".repeat(1000)))
                .post("/movies")
                .then()
                .statusCode(400)
                .body("title", Matchers.equalTo("title too long!"))
                .body("$", Matchers.aMapWithSize(1));
    }

    @Test
    void shouldCallMovieDescriptionService() {
        given()
                .port(app.getMappedPort(8080))
                .when()
                .contentType("application/json")
                .body("""
            {"id": 42,"title": "Foo","type": "NEW"}
            """)
                .post("/movies")
                .then()
                .statusCode(200);

        given()
                .port(app.getMappedPort(8080))
                .when()
                .get("/movies/{id}", 42)
                .then()
                .body("id", Matchers.equalTo(42))
                .body("description", Matchers.equalTo("test description"))
                .statusCode(200);

    }

    private static class ApplicationContainer extends GenericContainer<ApplicationContainer> {

        ApplicationContainer() {
            super(DockerImageName.parse("rental-store:snapshot"));
        }
    }

    private static class WireMockContainer extends GenericContainer<WireMockContainer> {

        WireMockContainer() {
            super(DockerImageName.parse("wiremock/wiremock:3.3.1-alpine"));
        }

        public WireMock newClient() {
            return new WireMock(getHost(), getMappedPort(WIREMOCK_PORT));
        }
    }

    private static class PostgresTestContainer extends PostgreSQLContainer{

        public PostgresTestContainer(String dockerImageName) {
            super(dockerImageName);
        }

        public void truncateAll() {
            try (Connection conn = DriverManager.getConnection(
                    getJdbcUrl(), getUsername(), getPassword());
                 Statement stmt = conn.createStatement()) {

                stmt.execute("""
                TRUNCATE TABLE movies CASCADE
            """);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
