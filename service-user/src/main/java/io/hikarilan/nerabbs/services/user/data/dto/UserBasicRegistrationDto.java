package io.hikarilan.nerabbs.services.user.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserBasicRegistrationDto(@NotNull @NotBlank String username,
                                       @NotNull @Email String email,
                                       @NotNull String password) {
}
