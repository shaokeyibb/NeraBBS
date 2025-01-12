package io.hikarilan.nerabbs.services.post.data.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record PostCreationDto(
        @Nullable @Length(max = 100) String title,
        @NotNull @NotBlank @Length(max = 65535) String content
) {
}
