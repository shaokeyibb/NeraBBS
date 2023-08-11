package io.hikarilan.nerabbs.services.post.data.vo;

import io.hikarilan.nerabbs.services.post.database.entity.PostEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostVo(long id,
                     long posterID,
                     @Nullable @NotBlank String title,
                     @NotNull @NotBlank String content,
                     @NotNull String createTime) {

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
