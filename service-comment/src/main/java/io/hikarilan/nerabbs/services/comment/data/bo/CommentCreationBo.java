package io.hikarilan.nerabbs.services.comment.data.bo;

import io.hikarilan.nerabbs.services.comment.data.dto.CommentCreationDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CommentCreationBo(
        long commenterID,
        long postID,
        @Nullable Long parentCommentID,
        @NotNull @NotBlank @Length(max = 65535) String content
) {

    public static CommentCreationBo from(long userID, long postID, CommentCreationDto commentCreationDto) {
        return new CommentCreationBo(userID, postID, commentCreationDto.parentCommentID(), commentCreationDto.content());
    }

}
