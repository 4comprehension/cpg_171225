package com.pivovarit.moviedescriptions.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Component
public class RentalStoreClient {

    private final RestClient restClient;

    public RentalStoreClient(String baseUrl, RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public boolean movieExists(long movieId) {
        try {
            var response = restClient.get()
                .uri("/movies/{id}", movieId)
                .retrieve()
                .body(MovieResponse.class);
            return response != null;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    public Optional<String> fetchDescription(long movieId) {
        try {
            var response = restClient.get()
                .uri("/movies/{id}", movieId)
                .retrieve()
                .body(MovieResponse.class);
            if (response != null && response.description() != null) {
                return Optional.of(response.description());
            }
            return Optional.empty();
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    record MovieResponse(long id, String title, String type, String description) {
    }
}
