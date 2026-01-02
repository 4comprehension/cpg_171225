package com.pivovarit.moviedescriptions;

import com.pivovarit.moviedescriptions.repository.JdbiH2MovieDescriptionsRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "server.error.include-message=always",
                "server.error.include-binding-errors=always",
                "server.error.include-stacktrace=never"
        }
)
class MovieDescriptionIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbiH2MovieDescriptionsRepository movieRepository;

        @BeforeEach
        void setUp() {
            movieRepository.clean();
        }

    @Test
    void shouldCreateMovieDescription() {
        given()
                .port(port)
                .when()
                .get("/movie-descriptions")
                .then()
                .body("", Matchers.hasSize(0))
                .statusCode(200);

        given()
                .port(port)
                .when()
                .contentType("application/json")
                .body("""
        {"id": 42,"description": "Bar"}
        """)
                .post("/movie-descriptions")
                .then()
                .statusCode(200);

        given()
                .port(port)
                .when()
                .get("/movie-descriptions")
                .then()
                .body("", Matchers.hasSize(1))
                .body("find { it.description == 'Bar' }.id", Matchers.equalTo(42))
                .statusCode(200);

        given()
                .port(port)
                .when()
                .get("/movie-descriptions/{id}", 42)
                .then()
                .body("id", Matchers.equalTo(42))
                .body("description", Matchers.equalTo("Bar"))
                .statusCode(200);
    }

    @Test
    void shouldRejectMovieDescriptionWithNegativeId() {
        given()
                .port(port)
                .when()
                .contentType("application/json")
                .body("""
        {"id": -42,"description": "Bar"}
        """)
                .post("/movie-descriptions")
                .then()
                .statusCode(400)
                .body("errors.find {field='id'}.defaultMessage",  Matchers.is("must be greater than 0"));
    }

    @Test
    void shouldRejectMovieDescriptionWithTooLongDescription() {
        given()
                .port(port)
                .when()
                .contentType("application/json")
                .body("""
         {"id": 42,"description": "%s"}
        """.formatted("a".repeat(1000)))
                .post("/movie-descriptions")
                .then()
                .statusCode(400)
                .body("errors.find {field='description'}.defaultMessage", Matchers.equalTo("description too long!"));
    }
}


