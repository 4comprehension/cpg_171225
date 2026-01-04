package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Repository
@Profile("prod")
@Primary
public class MovieDescriptionsHttpClient implements MovieDescriptionsRepository {

    private static final Logger log = LoggerFactory.getLogger(MovieDescriptionsHttpClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public MovieDescriptionsHttpClient(RestTemplate restTemplate,
                                       @Value("${movie.service.url:http://localhost:8081}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        log.info("MovieDescriptionsHttpClient created, baseUrl={}", baseUrl);
    }

    @Override
    public Optional<MovieDescription> findByMovieId(MovieId movieId) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment("movies", String.valueOf(movieId.id()), "description")
                .toUriString();

        log.debug("Calling movie-descriptions service at {}", url);
        try {
            RemoteMovieDescription resp = restTemplate.getForObject(url, RemoteMovieDescription.class);
            if (resp == null) {
                log.warn("movie-descriptions returned null for movieId={}", movieId.id());
                return Optional.empty();
            }
            MovieDescription local = new MovieDescription(resp.getDescription());
            return Optional.of(local);
        } catch (RestClientException e) {
            log.warn("Failed to call movie-descriptions for movieId={}, url={}, error={}", movieId.id(), url, e.toString());
            return Optional.empty();
        }
    }

    // helper DTO for remote JSON
    public static class RemoteMovieDescription {
        private long movieId;
        private String description;

        public RemoteMovieDescription() {}

        public long getMovieId() { return movieId; }
        public void setMovieId(long movieId) { this.movieId = movieId; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
