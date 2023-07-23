package io.hikarilan.nerabbs.services.auth.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserBasicSignInDto(@NotNull @Email String email,
                                 @NotNull String password) {
}
