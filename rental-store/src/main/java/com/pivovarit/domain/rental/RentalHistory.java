package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import com.pivovarit.domain.rental.api.MovieRentalEvent;

import java.util.List;

interface RentalHistory {
    void saveRentEvent(MovieId id, String login, long eventId);

    void saveReturnEvent(MovieId id, String login, long eventId);

    long lastEventId();

    List<MovieRentalEvent> findAll();

    List<MovieRentalEvent> findUserRentals(String login);

    List<MovieRentalEvent> findMovieRentals(MovieId movieId);

    List<MovieRentalEvent> getUnprocessed();

    void markProcessed(MovieRentalEvent event);
}
