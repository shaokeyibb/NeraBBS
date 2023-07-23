package io.hikarilan.nerabbs.services.auth.service;

import io.hikarilan.nerabbs.lib.services.user.grpc.RegisterUserRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserRegistrationGrpc;
import io.hikarilan.nerabbs.services.auth.data.dto.UserBasicSignUpDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class BasicRegistrationService {

    @GrpcClient("nerabbs-service-user")
    private UserRegistrationGrpc.UserRegistrationBlockingStub userRegistrationStub;

    public long registerUser(@NotNull @Valid UserBasicSignUpDto userBasicSignInDto) {
        var resp = userRegistrationStub.registerUser(RegisterUserRequest.newBuilder()
                .setUsername(userBasicSignInDto.username())
                .setEmail(userBasicSignInDto.email())
                .setPassword(userBasicSignInDto.password())
                .build());

        return resp.getId();
    }

}
