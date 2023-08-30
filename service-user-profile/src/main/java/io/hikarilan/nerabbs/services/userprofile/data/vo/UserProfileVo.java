package io.hikarilan.nerabbs.services.userprofile.data.vo;


import io.hikarilan.nerabbs.services.userprofile.database.entity.UserProfileEntity;
import jakarta.validation.constraints.NotNull;

public record UserProfileVo(
        long userID,
        String username,
        String avatarPath,
        String signature
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
