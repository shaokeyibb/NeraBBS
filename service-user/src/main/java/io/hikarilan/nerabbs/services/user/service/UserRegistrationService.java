package io.hikarilan.nerabbs.services.user.service;

import io.hikarilan.nerabbs.services.user.data.dto.UserBasicRegistrationDto;
import io.hikarilan.nerabbs.services.user.database.entity.UserEntity;
import io.hikarilan.nerabbs.services.user.database.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
public class UserRegistrationService {

    private final UserRepository userRepository;

    public long registerUser(@Valid @NotNull UserBasicRegistrationDto userBasicRegistrationDto) {
        if (userRepository.existsByEmail(userBasicRegistrationDto.email()))
            throw new IllegalArgumentException("Email already exists");

        var saved = userRepository.save(UserEntity.fromUserBasicRegistrationDto(userBasicRegistrationDto));

        return saved.getId();
    }

}
