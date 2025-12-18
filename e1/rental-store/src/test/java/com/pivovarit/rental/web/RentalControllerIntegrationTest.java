package com.pivovarit.rental.web;

import com.pivovarit.rental.persistence.InMemoryMovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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
    }
}
