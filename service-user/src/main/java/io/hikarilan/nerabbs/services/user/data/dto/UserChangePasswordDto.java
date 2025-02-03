package io.hikarilan.nerabbs.services.user.data.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserChangePasswordDto(
        @NotNull @Length(min = 8) String newPassword
) {
}
