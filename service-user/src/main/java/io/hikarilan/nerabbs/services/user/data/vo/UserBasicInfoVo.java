package io.hikarilan.nerabbs.services.user.data.vo;

import io.hikarilan.nerabbs.services.user.database.entity.UserEntity;
import jakarta.validation.constraints.NotNull;

public record UserBasicInfoVo(long id,
                              String username,
                              String email,
                              String createAt
) {

    @NotNull
    public static UserBasicInfoVo fromUserEntity(@NotNull UserEntity entity) {
        return new UserBasicInfoVo(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getCreateAt().toInstant().toString());
    }

}
