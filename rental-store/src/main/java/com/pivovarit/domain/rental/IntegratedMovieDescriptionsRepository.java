package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.springframework.web.client.RestClient;

import java.util.Optional;

class IntegratedMovieDescriptionsRepository implements MovieDescriptionsRepository {

    private final RestClient restClient;

    IntegratedMovieDescriptionsRepository(String baseUrl, RestClient.Builder restClient) {
        this.restClient = restClient.baseUrl(baseUrl).build();
    }

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        try {
            MovieDescription movieDescription = restClient.get()
                    .uri("/movie-descriptions/{id}", movieId.id())
                    .retrieve()
                    .body(MovieDescription.class);
            return Optional.ofNullable(movieDescription);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

