package com.pivovarit.domain.rental.jpa;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PersistedMovie {

    @Id
    private long id;

    private String title;
    private String type;

    @OneToMany(
      mappedBy = "movie",
      cascade = CascadeType.ALL,
      orphanRemoval = true
    )
    private List<PersistedRental> rentals = new ArrayList<>();

    public PersistedMovie() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PersistedMovie(long id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public void addRental(String renter) {
        rentals.add(new PersistedRental(this, renter));
    }

    public void removeRental(PersistedRental rental) {
        rentals.remove(rental);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public List<PersistedRental> getRentals() {
        return rentals;
    }

    @Override
    public String toString() {
        return "PersistedMovie{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", type='" + type + '\'' +
               ", rentals=" + rentals +
               '}';
    }
}
