package com.pivovarit.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

public class RentalStoreClient {

    private final RestClient restClient;

    public RentalStoreClient(String baseUrl, RestClient.Builder restClient) {
        this.restClient = restClient.baseUrl(baseUrl).build();
    }

    public List<Movie> findAllMovies() {
        return restClient.get()
          .uri("/movies")
          .retrieve()
          .body(ParameterizedTypeReference.forType(List.class));
    }

    public List<Movie> findAllMoviesByType(String type) {
        return restClient.get()
          .uri("/movies?type={type}", type)
          .retrieve()
          .body(ParameterizedTypeReference.forType(List.class));
    }

    public Optional<Movie> findMovieById(long id) {
        try {
            Movie movie = restClient.get()
              .uri("/movies/{id}", id)
              .retrieve()
              .body(Movie.class);
            return Optional.ofNullable(movie);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void saveMovie(MovieAddRequest request) {
        restClient.post()
          .uri("/movies")
          .contentType(MediaType.APPLICATION_JSON)
          .body(request)
          .retrieve()
          .toBodilessEntity();
    }
}
