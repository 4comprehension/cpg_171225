package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MoviePriceCalculatorTest {

    @Test
    void example_() throws Exception {
        MoviePriceCalculator moviePriceCalculator = new MoviePriceCalculator(Map.of("new", 42));

        int result = moviePriceCalculator.calculatePrice(new Movie(new MovieId(42), "result", MovieType.NEW));

        System.out.println(result);
    }
}
