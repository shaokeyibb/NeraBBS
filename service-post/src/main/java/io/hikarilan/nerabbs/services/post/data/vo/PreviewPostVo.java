package io.hikarilan.nerabbs.services.post.data.vo;

import io.hikarilan.nerabbs.services.post.database.entity.PostEntity;
import jakarta.validation.constraints.NotNull;

public record PreviewPostVo(long id,
                            long posterID,
                            String title,
                            String content,
                            String createAt) {

    @NotNull
    public static PreviewPostVo fromPostEntity(@NotNull PostEntity entity) {
        return new PreviewPostVo(
                entity.getId(),
                entity.getPosterID(),
                entity.getTitle(),
                entity.getContent().substring(0, Math.min(entity.getContent().length(), 201)),
                entity.getCreateAt().toInstant().toString()
        );
    }


}
