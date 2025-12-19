package com.pivovarit.blackbox;

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
class BlackboxTest {

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
    static ApplicationContainer app = new ApplicationContainer()
      .dependsOn(postgres)
      .withNetwork(network)
      .withEnv("APPLICATION_PROFILE", "prod")
      .withEnv("POSTGRES_URL", "jdbc:postgresql://postgres:5433/postgres")
      .withEnv("POSTGRES_USER", "postgres")
      .withEnv("POSTGRES_PASSWORD", "password")
      .withExposedPorts(8080)
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("rental-store"))
      .waitingFor(Wait.forHttp("/health").forStatusCode(200));

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

    private static class ApplicationContainer extends GenericContainer<ApplicationContainer> {
        ApplicationContainer() {
            super(DockerImageName.parse("rental-store:snapshot"));
        }
    }
}
