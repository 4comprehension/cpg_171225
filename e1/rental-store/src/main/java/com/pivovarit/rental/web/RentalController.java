package com.pivovarit.rental.web;

import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.model.MovieDto;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.model.MovieType;
import com.pivovarit.rental.service.RentalService;
import org.springframework.http.ResponseEntity;
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

    private final RentalService rentalService;

    RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/movies")
    public ResponseEntity<Collection<MovieDto>> moviesByType(@RequestParam(required = false) Optional<String> type) {
        if (type.isPresent()) {
            return isValid(type.get())
              ? ResponseEntity.ok(rentalService.findMovieByType(type.get()))
              : ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(rentalService.findAllMovies());
    }

    private boolean isValid(String type) {
        return MovieType.exists(type);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDto> movieById(@PathVariable long id) {
        return ResponseEntity.of(rentalService.findMovieById(new MovieId(id)));
    }

    @PostMapping("/movies")
    public void addMovie(@RequestBody MovieAddRequest movieDto) {
        rentalService.addMovie(movieDto);
    }

    // RentalService
    // GET /movies (zwracanie wszystkich filmów)
    // GET /movies?type=NEW (filtracja po typie filmu)
    // GET /movies/{id} (pobieranie filmu po ID)
    // POST /movies (dodawanie nowego filmu)
}
