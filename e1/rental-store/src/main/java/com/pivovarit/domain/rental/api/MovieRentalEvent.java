package com.pivovarit.domain.rental.api;

public record MovieRentalEvent(long id, String timestamp, MovieRentalEventType type, Long movieId, String login) {
}
