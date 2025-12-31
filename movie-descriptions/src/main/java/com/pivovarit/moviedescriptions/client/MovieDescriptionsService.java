package com.pivovarit.moviedescriptions.client;

import com.pivovarit.moviedescriptions.domain.MovieDescriptionEntity;
import com.pivovarit.moviedescriptions.domain.MovieDescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieDescriptionsService {

    private final RentalStoreClient rentalStoreClient;
    private final MovieDescriptionRepository repository;

    public MovieDescriptionsService(RentalStoreClient rentalStoreClient, MovieDescriptionRepository repository) {
        this.rentalStoreClient = rentalStoreClient;
        this.repository = repository;
    }

    public void addDescription(long movieId, String description) {
        if (!rentalStoreClient.movieExists(movieId)) {
            throw new IllegalArgumentException("Movie with id " + movieId + " does not exist");
        }
        repository.save(new MovieDescriptionEntity(movieId, description));
    }

    public void updateDescription(long movieId, String description) {
        if (!rentalStoreClient.movieExists(movieId)) {
            throw new IllegalArgumentException("Movie with id " + movieId + " does not exist");
        }
        repository.findById(movieId)
            .ifPresentOrElse(entity -> {
                entity.setDescription(description);
                repository.save(entity);
            }, () -> repository.save(new MovieDescriptionEntity(movieId, description)));
    }

    public Optional<MovieDescriptionDto> getDescription(long movieId) {
        return repository.findById(movieId)
            .map(e -> new MovieDescriptionDto(movieId, e.getDescription()));
    }

    public void deleteDescription(long movieId) {
        repository.deleteById(movieId);
    }
}
