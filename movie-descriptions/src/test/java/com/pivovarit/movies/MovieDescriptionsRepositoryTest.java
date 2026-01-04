package com.pivovarit.movies;

import com.pivovarit.movies.domain.MovieDescriptionsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.flyway.enabled=true"
})
class MovieDescriptionsRepositoryTest {

    @Autowired
    MovieDescriptionsRepository repository;

    @Test
    void shouldSaveAndReadMovieDescription() {
        repository.save(1L, "Test description");

        Optional<String> result = repository.findDescriptionByMovieId(1L);

        assertThat(result)
                .isPresent()
                .contains("Test description");
    }
}
