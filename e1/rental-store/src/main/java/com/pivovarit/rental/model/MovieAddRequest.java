package com.pivovarit.rental.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovieAddRequest(
  @Positive long id,
  @NotBlank @Size(max = 255) String title,
  @NotNull(message = "type is obligatory!") String type) {
}
