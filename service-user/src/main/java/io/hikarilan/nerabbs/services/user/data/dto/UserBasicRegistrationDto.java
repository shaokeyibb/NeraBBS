package io.hikarilan.nerabbs.services.user.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserBasicRegistrationDto(@NotNull @Length(min = 3) @NotBlank String username,
                                       @NotNull @Email String email,
                                       @NotNull @Length(min = 8) String password) {
}
