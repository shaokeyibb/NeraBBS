package io.hikarilan.nerabbs.services.post.data.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreationDto(
        @Nullable String title,
        @NotNull @NotBlank String content
) {
}
