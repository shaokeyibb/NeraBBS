package io.hikarilan.nerabbs.services.userprofile.data.vo;


import io.hikarilan.nerabbs.services.userprofile.database.entity.UserProfileEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record UserProfileVo(
        long userID,
        @Nullable String username,
        @Nullable String avatarPath,
        @Nullable String signature
) {

    @NotNull
    public static UserProfileVo fromUserProfileEntity(@NotNull UserProfileEntity entity) {
        return new UserProfileVo(
                entity.getUserID(),
                entity.getUsername(),
                entity.getAvatarPath(),
                entity.getSignature()
        );
    }

}
