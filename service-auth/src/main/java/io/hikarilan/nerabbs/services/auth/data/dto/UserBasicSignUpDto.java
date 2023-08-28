package io.hikarilan.nerabbs.services.auth.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserBasicSignUpDto(@NotNull @Email String email,
                                 @NotNull @Length(min = 8) String password) {
}
