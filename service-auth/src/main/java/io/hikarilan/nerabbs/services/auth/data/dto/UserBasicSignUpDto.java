package io.hikarilan.nerabbs.services.auth.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserBasicSignUpDto(@NotNull @NotBlank String username,
                                 @NotNull @Email String email,
                                 @NotNull String password) {
}
