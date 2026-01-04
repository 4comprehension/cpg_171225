package org.example.moviedescriptions.controller;

import jakarta.validation.Valid;
import org.example.moviedescriptions.dto.MovieDescriptionDto;
import org.example.moviedescriptions.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {
    
    private final MovieService movieService;
    
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    
    @PostMapping
    public ResponseEntity<Void> addMovie(@Valid @RequestBody MovieDescriptionDto movieDescriptionDto) {
        movieService.addMovie(movieDescriptionDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDescriptionDto> getMovieDescription(@PathVariable Long movieId) {
        return movieService.getMovieDescription(movieId)
                .map(movie -> ResponseEntity.ok(movie))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDescriptionDto> updateMovieDescription(
            @PathVariable Long movieId,
            @Valid @RequestBody MovieDescriptionDto requestDto) {
        return movieService.updateMovieDescription(movieId, requestDto)
                .map(movie -> ResponseEntity.ok(movie))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}