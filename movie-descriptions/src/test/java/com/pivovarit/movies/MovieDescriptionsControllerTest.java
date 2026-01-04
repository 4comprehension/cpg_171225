package com.pivovarit.movies;

import com.pivovarit.movies.api.CreateDescriptionRequest;
import com.pivovarit.movies.api.MovieDescriptionResponse;
import com.pivovarit.movies.api.UpdateDescriptionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieDescriptionsControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    void cleanDb() {
        jdbc.update("DELETE FROM movie_descriptions");
    }

    @Test
    void shouldCreateReadAndUpdateDescription() {
        String base = "http://localhost:" + port;

        // create
        CreateDescriptionRequest create = new CreateDescriptionRequest(1, "Initial description");
        rest.postForEntity(base + "/movies", create, Void.class);

        // read
        MovieDescriptionResponse resp = rest.getForObject(base + "/movies/1/description", MovieDescriptionResponse.class);
        assertThat(resp).isNotNull();
        assertThat(resp.description()).isEqualTo("Initial description");

        // update
        UpdateDescriptionRequest update = new UpdateDescriptionRequest("Updated desc");
        rest.put(base + "/movies/1/description", update);

        // read again
        MovieDescriptionResponse resp2 = rest.getForObject(base + "/movies/1/description", MovieDescriptionResponse.class);
        assertThat(resp2).isNotNull();
        assertThat(resp2.description()).isEqualTo("Updated desc");
    }
}
