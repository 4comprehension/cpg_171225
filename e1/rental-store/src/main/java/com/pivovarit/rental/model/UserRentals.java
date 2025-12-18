package com.pivovarit.rental.model;

import com.pivovarit.rental.event.MovieRentalEvent;

import java.util.HashSet;
import java.util.Set;

public final class UserRentals implements RentalAggregate {

    private final String login;

    private final Set<MovieId> currentRentals = new HashSet<>();

    public UserRentals(String login) {
        this.login = login;
    }

    @Override
    public void apply(MovieRentalEvent event) {
        switch (event.type()) {
            case MOVIE_RENTED -> currentRentals.add(new MovieId(event.movieId()));
            case MOVIE_RETURNED -> currentRentals.remove(new MovieId(event.movieId()));
        }
    }

    public boolean canReturn(MovieId id) {
        return currentRentals.contains(id);
    }

    public boolean canRent() {
        return currentRentals.size() < 5;
    }

    @Override
    public String toString() {
        return "UserRentals{" +
          "login='" + login + '\'' +
          ", currentRentals=" + currentRentals +
          '}';
    }
}
