package com.pivovarit.rental.model;

import com.pivovarit.rental.event.MovieRentalEvent;

import java.util.List;

public sealed interface RentalAggregate permits Movie, UserRentals {
    void apply(MovieRentalEvent event);

    default void apply(List<MovieRentalEvent> events) {
        events.forEach(this::apply);
    }
}
