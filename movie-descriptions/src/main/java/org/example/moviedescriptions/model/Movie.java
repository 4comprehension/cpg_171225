package org.example.moviedescriptions.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    private Long movieId;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Column(length = 2000)
    private String description;
    
    public Movie() {}
    
    public Movie(Long movieId, String description) {
        this.movieId = movieId;
        this.description = description;
    }
    
    public Long getMovieId() {
        return movieId;
    }
    
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}