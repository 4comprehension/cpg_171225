package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import com.pivovarit.domain.rental.api.MovieRentalEvent;

import java.util.Optional;

final class Movie implements RentalAggregate {

    private final MovieId id;
    private final String title;
    private final MovieType type;

    private String renter;

    public Movie(MovieId id, String title, MovieType type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public boolean canBeRented() {
        return renter == null;
    }

    public boolean canBeReturned(String login) {
        return renter != null && renter.equals(login);
    }

    @Override
    public void apply(MovieRentalEvent event) {
        switch (event.type()) {
            case MOVIE_RENTED -> renter = event.login();
            case MOVIE_RETURNED -> renter = null;
        }
    }

    public MovieId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public MovieType getType() {
        return type;
    }

    Optional<String> getRenter() {
        return Optional.ofNullable(renter);
    }

    @Override
    public String toString() {
        return "Movie{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", type=" + type +
               ", renter='" + renter + '\'' +
               '}';
    }
}
