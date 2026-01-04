package org.example.moviedescriptions.service;

import org.example.moviedescriptions.dto.MovieDescriptionDto;
import org.example.moviedescriptions.model.Movie;
import org.example.moviedescriptions.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {
    
    private final MovieRepository movieRepository;
    
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    
    public void addMovie(MovieDescriptionDto requestDto) {
        // check if movie already exists
        if (movieRepository.existsById(requestDto.movieId())) {
            throw new IllegalArgumentException("Movie with ID " + requestDto.movieId() + " already exists.");
        }

        Movie movie = new Movie(requestDto.movieId(), requestDto.description());
        movieRepository.save(movie);
    }
    
    public Optional<MovieDescriptionDto> getMovieDescription(Long movieId) {
        return movieRepository.findById(movieId)
                .map(movie -> new MovieDescriptionDto(movie.getMovieId(), movie.getDescription()));
    }
    
    public Optional<MovieDescriptionDto> updateMovieDescription(Long movieId, MovieDescriptionDto requestDto) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    movie.setDescription(requestDto.description());
                    Movie updatedMovie = movieRepository.save(movie);
                    return new MovieDescriptionDto(updatedMovie.getMovieId(), updatedMovie.getDescription());
                });
    }
}