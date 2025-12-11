package com.pivovarit.movies.domain;

import org.junit.jupiter.api.Test;
import org.testcontainers.postgresql.PostgreSQLContainer;

class SomeTest {

    @Test
    void docker_test() {
        try (PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18")) {
            postgreSQLContainer.start();
        }
    }

    @Test
    void should_add_movie() {
    }
}
