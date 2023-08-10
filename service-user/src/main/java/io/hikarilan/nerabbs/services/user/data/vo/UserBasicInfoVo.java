package io.hikarilan.nerabbs.services.user.data.vo;

import io.hikarilan.nerabbs.services.user.database.entity.UserEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserBasicInfoVo(long id,
                              @NotNull @Length(min = 3) @NotBlank String username,
                              @Nullable @Email String email) {

    @NotNull
    public static UserBasicInfoVo fromUserEntity(@NotNull UserEntity entity) {
        return new UserBasicInfoVo(entity.getId(), entity.getUsername(), entity.getEmail());
    }

}
