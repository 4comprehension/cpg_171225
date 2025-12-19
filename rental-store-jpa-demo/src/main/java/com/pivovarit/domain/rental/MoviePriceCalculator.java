package com.pivovarit.domain.rental;

import java.util.Map;
import java.util.Objects;

class MoviePriceCalculator {

    private final Map<String, Integer> prices;

    /**
     * The Spring team generally advocates constructor injection, as it lets you implement
     * application components as immutable objects and
     * ensures that required dependencies are not null.
     * Furthermore, constructor-injected components
     * are always returned to the client (calling) code in a fully
     * initialized state. As a side note, a large number of
     * constructor arguments is a bad code smell,
     * implying that the class likely has too many responsibilities
     * and should be refactored to better address proper separation of concerns.
     */
    // https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html
    public MoviePriceCalculator(Map<String, Integer> prices) {
        this.prices = prices;
    }

    public int calculatePrice(Movie movie) {
        return Objects.requireNonNull(prices.get(movie.getType().toString().toLowerCase()));
    }
}
