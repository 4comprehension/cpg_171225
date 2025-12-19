package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

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
        movieRepository.save(MovieConverters.from(request));
    }

    public Collection<MovieDto> findAllMovies() {
        return movieRepository.findAll().stream().map(toMovieWithDescription()).toList();
    }

    public Collection<MovieDto> findMovieByType(String type) {
        return movieRepository.findByType(type).stream().map(toMovieWithDescription()).toList();
    }

    public Optional<MovieDto> findMovieById(MovieId id) {
        return movieRepository.findById(id).map(toMovieWithDescription());
    }

    private Function<Movie, MovieDto> toMovieWithDescription() {
        return m -> MovieConverters.from(m, movieDescriptionsRepository.findByMovieId(m.getId())
          .orElse(new MovieDescription("")).description());
    }
}
