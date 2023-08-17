package io.hikarilan.nerabbs.services.post.data.vo;

import io.hikarilan.nerabbs.services.post.database.entity.PostEntity;
import jakarta.validation.constraints.NotNull;

public record PostVo(long id,
                     long posterID,
                     String title,
                     String content,
                     String createAt) {

    @NotNull
    public static PostVo fromPostEntity(@NotNull PostEntity entity) {
        return new PostVo(
                entity.getId(),
                entity.getPosterID(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreateAt().toInstant().toString()
        );
    }

}
