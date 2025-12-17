package com.pivovarit;

import com.pivovarit.rental.MoviePriceCalculator;
import com.pivovarit.rental.config.CalculatorConfiguration;
import com.pivovarit.rental.model.Movie;
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
 * <p>
 * cennik dev:
 * NEW -> 198237
 * REGULAR -> 123
 * OLD -> 123891
 */
@Component
class Runner implements ApplicationRunner {

    private final RentalService rentalService;
    private final MoviePriceCalculator calculator;
    private final CalculatorConfiguration calculatorConfiguration;

    Runner(
      RentalService rentalService,
      MoviePriceCalculator calculator,
      CalculatorConfiguration calculatorConfiguration) {
        this.rentalService = rentalService;
        this.calculator = calculator;
        this.calculatorConfiguration = calculatorConfiguration;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Movie m1 = new Movie(new MovieId(42), "Spiderman", MovieType.NEW);
        Movie m2 = new Movie(new MovieId(1), "Tenet", MovieType.REGULAR);
        Movie m3 = new Movie(new MovieId(2), "Casablanca", MovieType.OLD);

        System.out.println("calculator.calculatePrice(m1) = " + calculator.calculatePrice(m1));
        System.out.println("calculator.calculatePrice(m2) = " + calculator.calculatePrice(m2));
        System.out.println("calculator.calculatePrice(m3) = " + calculator.calculatePrice(m3));

        System.out.println("calculatorConfiguration.pricing = " + calculatorConfiguration.pricing);
        System.out.println("calculatorConfiguration.getPricing() = " + calculatorConfiguration.getPricing());
    }
}
