package com.pivovarit.moviedescriptions.client;

import com.pivovarit.moviedescriptions.client.RentalStoreClient;
import com.pivovarit.moviedescriptions.client.MovieDescriptionDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieDescriptionsService {

    private final RentalStoreClient rentalStoreClient;

    public MovieDescriptionsService(RentalStoreClient rentalStoreClient) {
        this.rentalStoreClient = rentalStoreClient;
    }

    public void addDescription(long movieId, String description) {
        if (!rentalStoreClient.movieExists(movieId)) {
            throw new IllegalArgumentException("Movie with id " + movieId + " does not exist");
        }
        // Description is stored in rental-store via HTTP call (delegation)
    }

    public void updateDescription(long movieId, String description) {
        if (!rentalStoreClient.movieExists(movieId)) {
            throw new IllegalArgumentException("Movie with id " + movieId + " does not exist");
        }
        // Description is stored in rental-store via HTTP call (delegation)
    }

    public Optional<MovieDescriptionDto> getDescription(long movieId) {
        return rentalStoreClient.fetchDescription(movieId)
            .map(desc -> new MovieDescriptionDto(movieId, desc));
    }
}
