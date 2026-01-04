package com.pivovarit.movies.api;

import com.pivovarit.movies.domain.MovieDescriptionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieDescriptionsController {

    private final MovieDescriptionsRepository repo;

    public MovieDescriptionsController(MovieDescriptionsRepository repo) {
        this.repo = repo;
    }

    // create (or upsert)
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateDescriptionRequest req) {
        repo.save(req.movieId(), req.description());
        return ResponseEntity.ok().build();
    }

    // update
    @PutMapping("/{id}/description")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody UpdateDescriptionRequest req) {
        repo.save(id, req.description());
        return ResponseEntity.ok().build();
    }

    // get
    @GetMapping("/{id}/description")
    public ResponseEntity<MovieDescriptionResponse> get(@PathVariable long id) {
        return repo.findDescriptionByMovieId(id)
                .map(desc -> ResponseEntity.ok(new MovieDescriptionResponse(id, desc)))
                .orElseGet(() -> ResponseEntity.ok(new MovieDescriptionResponse(id, "")));
    }
}
