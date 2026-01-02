package com.pivovarit.moviedescriptions.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateMovieDescriptionDTO(
        @Positive(message = "must be greater than 0") long id,
        @NotBlank @Size(max = 255, message = "description too long!") String description) {
}