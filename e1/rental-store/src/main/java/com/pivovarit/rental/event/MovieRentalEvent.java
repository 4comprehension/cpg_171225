package com.pivovarit.rental.event;

import com.pivovarit.rental.model.MovieRentalEventType;

public record MovieRentalEvent(long id, String timestamp, MovieRentalEventType type, Long movieId, String login) {
}
