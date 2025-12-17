package com.pivovarit;

import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.service.RentalService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
class Runner implements ApplicationRunner {

    private final RentalService rentalService;

    Runner(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        rentalService.addMovie(new MovieAddRequest(42, "Spiderman", "NEW"));
        System.out.println("rentalService.findAllMovies() = " + rentalService.findAllMovies());
    }
}
