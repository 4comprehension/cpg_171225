package com.pivovarit.web;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDescriptionDto;
import com.pivovarit.domain.rental.api.MovieDescriptionRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import com.pivovarit.domain.rental.RentalFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
public class RentalController {

    private final RentalFacade rentalFacade;

    RentalController(RentalFacade rentalFacade) {
        this.rentalFacade = rentalFacade;
    }

    @GetMapping("/movies")
    public ResponseEntity<Collection<MovieDto>> moviesByType(@RequestParam(required = false) Optional<String> type) {
        if (type.isPresent()) {
            return isValid(type.get())
              ? ResponseEntity.ok(rentalFacade.findMovieByType(type.get()))
              : ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(rentalFacade.findAllMovies());
    }

    private boolean isValid(String type) {
        return true; // TODO
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDto> movieById(@PathVariable long id) {
        return ResponseEntity.of(rentalFacade.findMovieById(new MovieId(id)));
    }

    @PostMapping("/movies")
    public void addMovie(@Validated @RequestBody MovieAddRequest movieDto) {
        rentalFacade.addMovie(movieDto);
    }

    @GetMapping("/movies/{id}/descriptions")
    public ResponseEntity<MovieDescriptionDto> movieDescriptionById(@PathVariable long id) {
        return ResponseEntity.of(rentalFacade.findDescriptionByMovieId(new MovieId(id)));
    }

    @PostMapping("/movies/{id}/descriptions")
    public ResponseEntity<MovieDescriptionDto> addDescriptionByMovieId(@PathVariable long id, @RequestBody MovieDescriptionRequest descriptionRequest) {
        return ResponseEntity.ok(rentalFacade.addDescriptionByMovieId(new MovieId(id), descriptionRequest.description()));
    }

    @PutMapping("/movies/{id}/descriptions")
    public ResponseEntity<MovieDescriptionDto> updateDescriptionByMovieId(@PathVariable long id, @RequestBody MovieDescriptionRequest descriptionRequest) {
        return ResponseEntity.ok(rentalFacade.updateDescriptionByMovieId(new MovieId(id), descriptionRequest.description()));
    }
}
