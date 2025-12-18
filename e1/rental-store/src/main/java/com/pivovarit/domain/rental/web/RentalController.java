package com.pivovarit.domain.rental.web;

import com.pivovarit.domain.rental.api.MovieAddRequest;
import com.pivovarit.domain.rental.api.MovieDto;
import com.pivovarit.domain.rental.api.MovieId;
import com.pivovarit.domain.rental.RentalFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
}
