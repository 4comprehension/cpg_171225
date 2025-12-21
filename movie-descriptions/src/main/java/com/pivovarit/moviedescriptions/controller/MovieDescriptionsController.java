package com.pivovarit.moviedescriptions.controller;

import com.pivovarit.moviedescriptions.dto.MovieDescriptionDto;
import com.pivovarit.moviedescriptions.service.MovieDescriptionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie-descriptions")
public class MovieDescriptionsController {

    private final MovieDescriptionsService movieDescriptionsService;

    public MovieDescriptionsController(MovieDescriptionsService movieDescriptionsService) {
        this.movieDescriptionsService = movieDescriptionsService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody MovieDescriptionDto dto) {
        movieDescriptionsService.addDescription(dto.movieId(), dto.description());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Void> update(@PathVariable long movieId, @RequestBody MovieDescriptionDto dto) {
        movieDescriptionsService.updateDescription(movieId, dto.description());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDescriptionDto> get(@PathVariable long movieId) {
        return movieDescriptionsService.getDescription(movieId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
