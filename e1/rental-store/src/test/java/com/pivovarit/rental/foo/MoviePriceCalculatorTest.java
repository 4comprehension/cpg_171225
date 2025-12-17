package com.pivovarit.rental.foo;

import com.pivovarit.rental.MoviePriceCalculator;
import com.pivovarit.rental.model.Movie;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.model.MovieType;
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
