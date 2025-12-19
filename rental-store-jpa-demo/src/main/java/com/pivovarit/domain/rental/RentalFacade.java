package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import com.pivovarit.domain.rental.jpa.PersistedMovie;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public class RentalFacade {

    private static final Logger log = LoggerFactory.getLogger(RentalFacade.class);

    private final MovieRepository movieRepository;
    private final MovieDescriptionsRepository movieDescriptionsRepository;
    private final MovieRentalService movieRentalService;

    RentalFacade(MovieRepository movieRepository, MovieDescriptionsRepository movieDescriptionsRepository, MovieRentalService movieRentalService) {
        this.movieRepository = movieRepository;
        this.movieDescriptionsRepository = movieDescriptionsRepository;
        this.movieRentalService = movieRentalService;
    }

    public void rentMovie(String login, MovieId id) {
        movieRentalService.rentMovie(login, id);
    }

    public void returnMovie(String login, MovieId id) {
        movieRentalService.returnMovie(login, id);
    }

    public void addMovie(MovieAddRequest request) {
        PersistedMovie persistedMovie = new PersistedMovie();
        persistedMovie.setId(request.id());
        persistedMovie.setTitle(request.title());
        persistedMovie.setType(request.type());
        movieRepository.save(persistedMovie);
    }

    public Collection<MovieDto> findAllMovies() {
        return movieRepository.findAll()
          .stream()
          .map(m -> new MovieDto(m.getId(), m.getTitle(), m.getType(), ""))
          .toList();
    }

    public Collection<MovieDto> findMovieByType(String type) {
        return movieRepository.findByType(type)
          .stream()
          .map(m -> new MovieDto(m.getId(), m.getTitle(), m.getType(), ""))
          .toList();
    }

    @Transactional
    public Optional<MovieDto> findMovieById(MovieId id) {
        return movieRepository.findById(id.id()).map(m -> new MovieDto(m.getId(), m.getTitle(), m.getType(), ""));
    }
}
