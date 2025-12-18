package com.pivovarit.rental.web;

import com.pivovarit.rental.persistence.InMemoryMovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentalControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private InMemoryMovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository.clean();
    }

    @Test
    void shouldCreateMovie() {
        given()
          .port(port)
          .when()
          .get("/movies")
          .then()
          .body("", Matchers.hasSize(0))
          .statusCode(200);

        given()
          .port(port)
          .when()
          .contentType("application/json")
          .body("""
            {"id": 42,"title": "Foo","type": "NEW"}
            """)
          .post("/movies")
          .then()
          .statusCode(200);

        given()
          .port(port)
          .when()
          .get("/movies")
          .then()
          .body("", Matchers.hasSize(1))
          .body("find { it.title == 'Foo' }.id", Matchers.equalTo(42))
          .body("find { it.title == 'Foo' }.type", Matchers.equalTo("NEW"))
          .statusCode(200);

        given()
          .port(port)
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
          .port(port)
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
          .port(port)
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
    void shouldGetMoviesIndependentlyOfOrder() throws Exception {
        given()
          .port(port)
          .when()
          .contentType("application/json")
          .body("""
            {"id": 1,"title": "Spiderman","type": "NEW"}
            """)
          .post("/movies")
          .then()
          .statusCode(200);

        given()
          .port(port)
          .when()
          .contentType("application/json")
          .body("""
            {"id": 2,"title": "Tenet","type": "REGULAR"}
            """)
          .post("/movies")
          .then()
          .statusCode(200);

        given()
          .port(port)
          .when()
          .get("/movies")
          .then()
          .body("", Matchers.hasSize(2))
          .body("find { it.title == 'Spiderman' }.id", Matchers.equalTo(1))
          .body("find { it.title == 'Spiderman' }.type", Matchers.equalTo("NEW"))
          .body("find { it.title == 'Tenet' }.id", Matchers.equalTo(2))
          .body("find { it.title == 'Tenet' }.type", Matchers.equalTo("REGULAR"))
          .statusCode(200);

        given()
          .port(port)
          .when()
          .queryParam("type", "NEW")
          .get("/movies")
          .then()
          .body("", Matchers.hasSize(1))
          .body("find { it.title == 'Spiderman' }.id", Matchers.equalTo(1))
          .body("find { it.title == 'Spiderman' }.type", Matchers.equalTo("NEW"))
          .statusCode(200);

        given()
          .port(port)
          .when()
          .queryParam("type", "REGULAR")
          .get("/movies")
          .then()
          .body("", Matchers.hasSize(1))
          .body("find { it.title == 'Tenet' }.id", Matchers.equalTo(2))
          .body("find { it.title == 'Tenet' }.type", Matchers.equalTo("REGULAR"))
          .statusCode(200);

        given()
          .port(port)
          .when()
          .queryParam("type", "OLD")
          .get("/movies")
          .then()
          .body("", Matchers.hasSize(0))
          .statusCode(200);
    }
}
