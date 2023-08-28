package io.hikarilan.nerabbs.services.user.data.vo;

import io.hikarilan.nerabbs.services.user.database.entity.UserEntity;
import jakarta.validation.constraints.NotNull;

public record UserBasicInfoVo(long id,
                              String email,
                              String createAt
) {

    @NotNull
    public static UserBasicInfoVo fromUserEntity(@NotNull UserEntity entity) {
        return new UserBasicInfoVo(entity.getId(), entity.getEmail(), entity.getCreateAt().toInstant().toString());
    }

}
