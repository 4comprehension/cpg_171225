package com.pivovarit.moviedescriptions.service;

import com.pivovarit.moviedescriptions.model.converter.MovieDescriptionConverter;
import com.pivovarit.moviedescriptions.model.dto.request.CreateMovieDescriptionDTO;
import com.pivovarit.moviedescriptions.model.dto.response.MovieDescriptionDTO;
import com.pivovarit.moviedescriptions.model.entity.MovieId;
import com.pivovarit.moviedescriptions.repository.MovieDescriptionsRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieDescriptionsService {

    private final MovieDescriptionsRepository movieDescriptionsRepository;

    public MovieDescriptionsService(MovieDescriptionsRepository movieDescriptionsRepository) {
        this.movieDescriptionsRepository = movieDescriptionsRepository;
    }

    public MovieDescriptionDTO findMovieDescriptionById(MovieId movieId) {
        return MovieDescriptionConverter.from(movieDescriptionsRepository.findByMovieId(movieId).orElseThrow(() -> new RuntimeException("Description not found")));
    }

    public Collection<MovieDescriptionDTO> findAllMovieDescriptions() {
        return movieDescriptionsRepository.findAll().stream().map(MovieDescriptionConverter::from).collect(Collectors.toList());
    }

    public void addMovieDescription(CreateMovieDescriptionDTO movieDescriptionDTO) {
        movieDescriptionsRepository.save(MovieDescriptionConverter.from(movieDescriptionDTO));
    }

    public void deleteMovieDescription(MovieId movieId) {
        movieDescriptionsRepository.deleteById(movieId);
    }
}
