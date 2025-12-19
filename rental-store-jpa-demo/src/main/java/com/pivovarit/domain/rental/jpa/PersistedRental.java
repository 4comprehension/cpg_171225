package com.pivovarit.domain.rental.jpa;

import jakarta.persistence.*;

@Entity
public class PersistedRental {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id")
    private PersistedMovie movie;

    private String renter;

    protected PersistedRental() {
    }

    public PersistedRental(PersistedMovie movie, String renter) {
        this.movie = movie;
        this.renter = renter;
    }

    public Long getId() {
        return id;
    }

    public PersistedMovie getMovie() {
        return movie;
    }

    public String getRenter() {
        return renter;
    }

    @Override
    public String toString() {
        return "PersistedRental{" +
               "id=" + id +
               ", movie=" + movie.getId() +
               ", renter='" + renter + '\'' +
               '}';
    }
}
