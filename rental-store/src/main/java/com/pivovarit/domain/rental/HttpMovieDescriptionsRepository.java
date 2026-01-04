package com.pivovarit.domain.rental;


import com.pivovarit.domain.rental.api.MovieId;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.util.Optional;

class HttpMovieDescriptionsRepository implements MovieDescriptionsRepository {

    private final RestClient restClient;

    HttpMovieDescriptionsRepository(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        return restClient.get()
                .uri("/movie-descriptions/{movieId}", movieId.id())
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        MovieDescriptionResponse body = response.bodyTo(MovieDescriptionResponse.class);
                        return Optional.of(new MovieDescription(body.description()));
                    } else if (response.getStatusCode() == HttpStatusCode.valueOf(404)) {
                        return Optional.empty();
                    }
                    throw new RuntimeException("Failed to fetch movie description: " + response.getStatusCode());
                });
    }

    private record MovieDescriptionResponse(Long movieId, String description) {
    }
}
