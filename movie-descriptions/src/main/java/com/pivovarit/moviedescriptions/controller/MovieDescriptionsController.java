package com.pivovarit.moviedescriptions.controller;

import com.pivovarit.moviedescriptions.model.dto.request.CreateMovieDescriptionDTO;
import com.pivovarit.moviedescriptions.model.dto.response.MovieDescriptionDTO;
import com.pivovarit.moviedescriptions.service.MovieDescriptionsService;
import com.pivovarit.moviedescriptions.model.entity.MovieId;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
public class MovieDescriptionsController {
    private final MovieDescriptionsService movieDescriptionService;

    public MovieDescriptionsController(MovieDescriptionsService movieDescriptionService) {
        this.movieDescriptionService = movieDescriptionService;
    }

    @GetMapping("/movie-descriptions")
    public Collection<MovieDescriptionDTO> movieDescriptions() {
        return movieDescriptionService.findAllMovieDescriptions();
    }

    @GetMapping("/movie-descriptions/{id}")
    public MovieDescriptionDTO movieDescriptionById(@PathVariable long id) {
        return movieDescriptionService.findMovieDescriptionById(new MovieId(id));
    }

    @PostMapping("/movie-descriptions")
    public void addMovieDescription(@Validated @RequestBody CreateMovieDescriptionDTO movieDescriptionDTO) {
        movieDescriptionService.addMovieDescription(movieDescriptionDTO);
    }
    @DeleteMapping("/movie-descriptions/{id}")
    public void deleteMovieDescription(@PathVariable long id) {
        movieDescriptionService.deleteMovieDescription(new MovieId(id));
    }
}
