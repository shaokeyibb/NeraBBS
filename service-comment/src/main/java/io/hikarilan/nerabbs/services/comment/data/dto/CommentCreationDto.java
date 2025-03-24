package io.hikarilan.nerabbs.services.comment.data.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CommentCreationDto(
        @Nullable Long parentCommentID,
        @NotNull @NotBlank @Length(max = 65535) String content
) {
}
