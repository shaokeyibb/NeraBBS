package io.hikarilan.nerabbs.services.user.data.bo;

import io.hikarilan.nerabbs.services.user.data.dto.UserChangePasswordDto;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserChangePasswordBo(
        long userId,
        String newPassword
) {

    public static UserChangePasswordBo from(long userId, UserChangePasswordDto userChangePasswordDto) {
        return new UserChangePasswordBo(userId, userChangePasswordDto.newPassword());
    }

}
