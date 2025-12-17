package com.pivovarit.rental;

import com.pivovarit.rental.model.Movie;

public class MoviePriceCalculator {

    private final int newPrice;
    private final int oldPrice;
    private final int regularPrice;

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
    public MoviePriceCalculator(int newPrice, int oldPrice, int regularPrice) {
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.regularPrice = regularPrice;
    }

    public int calculatePrice(Movie movie) {
        return switch (movie.getType()) {
            case NEW -> newPrice;
            case REGULAR -> regularPrice;
            case OLD -> oldPrice;
        };
    }
}
