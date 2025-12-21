package com.pivovarit.moviedescriptions.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Component
public class RentalStoreClient {

    private final RestTemplate restTemplate;
    private final String rentalStoreUrl;

    public RentalStoreClient(RestTemplate restTemplate, @Value("${rentalstore.url}") String rentalStoreUrl) {
        this.restTemplate = restTemplate;
        this.rentalStoreUrl = rentalStoreUrl;
    }

    public boolean movieExists(long movieId) {
        try {
            var response = restTemplate.getForObject(rentalStoreUrl + "/movies/{id}", MovieResponse.class, movieId);
            return response != null;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    public Optional<String> fetchDescription(long movieId) {
        try {
            var response = restTemplate.getForObject(rentalStoreUrl + "/movies/{id}", MovieResponse.class, movieId);
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
