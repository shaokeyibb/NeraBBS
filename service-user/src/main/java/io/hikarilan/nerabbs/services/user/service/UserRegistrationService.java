package io.hikarilan.nerabbs.services.user.service;

import io.hikarilan.nerabbs.common.exception.UserAlreadyExistsException;
import io.hikarilan.nerabbs.lib.services.userprofile.grpc.UpdateUserProfileRequest;
import io.hikarilan.nerabbs.lib.services.userprofile.grpc.UserProfileGrpc;
import io.hikarilan.nerabbs.services.user.data.bo.UserChangePasswordBo;
import io.hikarilan.nerabbs.services.user.data.dto.UserBasicRegistrationDto;
import io.hikarilan.nerabbs.services.user.database.entity.UserEntity;
import io.hikarilan.nerabbs.services.user.database.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
public class UserRegistrationService {

    private final UserRepository userRepository;

    @GrpcClient("nerabbs-service-user-profile")
    private UserProfileGrpc.UserProfileBlockingStub userProfileStub;

    @GlobalTransactional
    @Transactional
    public long registerUser(@Valid @NotNull UserBasicRegistrationDto userBasicRegistrationDto) {
        if (userRepository.existsByEmail(userBasicRegistrationDto.email()))
            throw new UserAlreadyExistsException();

        var saved = userRepository.save(UserEntity.fromUserBasicRegistrationDto(userBasicRegistrationDto));

        userProfileStub.updateUserProfile(UpdateUserProfileRequest.newBuilder()
                .setId(saved.getId())
                .setUsername("User#" + saved.getId())
                .setSignature("No Signature yet")
                .build());

        return saved.getId();
    }

    @Transactional
    public void changePassword(@Valid @NotNull UserChangePasswordBo userChangePasswordBo) {
        var user = userRepository.findById(userChangePasswordBo.userId()).orElseThrow();
        user.changePassword(userChangePasswordBo.newPassword());
        userRepository.save(user);
    }
}
