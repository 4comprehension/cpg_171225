package com.pivovarit.rental.service;

import com.pivovarit.rental.model.Movie;
import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.model.MovieDescription;
import com.pivovarit.rental.model.MovieDto;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.persistence.InMemoryMovieDescriptionsRepository;
import com.pivovarit.rental.persistence.InMemoryMovieRepository;
import com.pivovarit.rental.util.MovieConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Component
public class RentalService {

    private static final Logger log = LoggerFactory.getLogger(RentalService.class);

    private final InMemoryMovieRepository movieRepository;
    private final InMemoryMovieDescriptionsRepository movieDescriptionsRepository;
    private final MovieRentalService movieRentalService;

    public RentalService(InMemoryMovieRepository movieRepository, InMemoryMovieDescriptionsRepository movieDescriptionsRepository, MovieRentalService movieRentalService) {
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
