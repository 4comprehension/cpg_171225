package com.pivovarit.domain.rental.api;

import jakarta.validation.constraints.NotNull;

public record MovieDescriptionRequest(
  @NotNull(message = "description is obligatory!") String description) {
}
