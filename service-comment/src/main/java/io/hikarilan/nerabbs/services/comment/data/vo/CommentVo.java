package io.hikarilan.nerabbs.services.comment.data.vo;

import io.hikarilan.nerabbs.services.comment.database.entity.CommentEntity;
import jakarta.validation.constraints.NotNull;

public record CommentVo(
        long id,
        long postID,
        long commenterID,
        long rootCommentID,
        Long parentCommentID,
        String content,
        String createAt,
        boolean isDeleted
) {

    @NotNull
    public static CommentVo fromCommentEntity(@NotNull CommentEntity entity) {
        return new CommentVo(
                entity.getId(),
                entity.getPostID(),
                entity.getCommenterID(),
                entity.getRootComment().getId(),
                entity.getParentComment() == null ? null : entity.getParentComment().getId(),
                entity.isFrozen() ? "*This comment has been deleted*" : entity.getContent(),
                entity.getCreateAt().toInstant().toString(),
                entity.isFrozen()
        );
    }

}
