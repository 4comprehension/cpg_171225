package com.pivovarit.moviedescriptions.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movie_descriptions")
public class MovieDescriptionEntity {

    @Id
    private Long movieId;

    @Column(nullable = false)
    private String description;

    protected MovieDescriptionEntity() {
    }

    public MovieDescriptionEntity(Long movieId, String description) {
        this.movieId = movieId;
        this.description = description;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
