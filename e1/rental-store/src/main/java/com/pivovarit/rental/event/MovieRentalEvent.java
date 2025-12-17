package com.pivovarit.rental.event;

import com.pivovarit.rental.model.MovieRentalEventType;
import com.pivovarit.rental.model.MovieId;

public record MovieRentalEvent(long id, String timestamp, MovieRentalEventType type, MovieId movieId, String login) {
}
