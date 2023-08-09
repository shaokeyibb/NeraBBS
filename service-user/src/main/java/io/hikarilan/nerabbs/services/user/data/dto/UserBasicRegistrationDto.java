package io.hikarilan.nerabbs.services.user.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserBasicRegistrationDto(@NotNull @Min(3) @NotBlank String username,
                                       @NotNull @Email String email,
                                       @NotNull @Min(8) String password) {
}
