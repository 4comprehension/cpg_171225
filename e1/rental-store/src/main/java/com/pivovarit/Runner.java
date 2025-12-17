package com.pivovarit;

import com.pivovarit.rental.model.Movie;
import com.pivovarit.rental.model.MovieAddRequest;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.model.MovieType;
import com.pivovarit.rental.service.RentalService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * MoviePriceCalculator
 * cennik produkcyjny:
 * NEW -> 10
 * REGULAR -> 5
 * OLD -> 3
 *
 * cennik dev:
 * NEW -> 198237
 * REGULAR -> 123
 * OLD -> 123891
 */
@Component
class Runner implements ApplicationRunner {

    private final RentalService rentalService;
//    private final MoviePriceCalculator calculator;

    Runner(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Movie m1 = new Movie(new MovieId(42), "Spiderman", MovieType.NEW);
        Movie m2 = new Movie(new MovieId(1), "Tenet", MovieType.REGULAR);
        Movie m3 = new Movie(new MovieId(2), "Casablanca", MovieType.OLD);

//        calculator...
    }
}
