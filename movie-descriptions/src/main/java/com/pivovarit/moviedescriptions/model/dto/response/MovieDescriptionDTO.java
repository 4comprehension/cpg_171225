package com.pivovarit.moviedescriptions.model.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovieDescriptionDTO(
    @Positive long id,
    @NotBlank @Size(max = 255, message = "description too long!") String description) {
}