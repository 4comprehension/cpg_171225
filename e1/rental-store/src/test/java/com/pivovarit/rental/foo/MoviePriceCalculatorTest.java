package com.pivovarit.rental.foo;

import com.pivovarit.rental.MoviePriceCalculator;
import com.pivovarit.rental.model.Movie;
import com.pivovarit.rental.model.MovieId;
import com.pivovarit.rental.model.MovieType;
import org.junit.jupiter.api.Test;

class MoviePriceCalculatorTest {

    @Test
    void example_() throws Exception {
        MoviePriceCalculator moviePriceCalculator = new MoviePriceCalculator(1, 2, 3);

        int result = moviePriceCalculator.calculatePrice(new Movie(new MovieId(42), "result", MovieType.NEW));

        System.out.println(result);
    }
}
