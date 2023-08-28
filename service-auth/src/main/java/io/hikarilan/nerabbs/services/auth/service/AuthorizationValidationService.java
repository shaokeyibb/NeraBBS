package io.hikarilan.nerabbs.services.auth.service;

import io.hikarilan.nerabbs.lib.services.user.grpc.BasicUserInfoRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.CheckPasswordRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserAuthorizationGrpc;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserInfoGrpc;
import io.hikarilan.nerabbs.services.auth.data.dto.UserBasicSignInDto;
import io.hikarilan.nerabbs.services.auth.exception.UserInfoMismatchException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AuthorizationValidationService {

    @GrpcClient("nerabbs-service-user")
    private UserAuthorizationGrpc.UserAuthorizationBlockingStub userAuthorizationStub;

    @GrpcClient("nerabbs-service-user")
    private UserInfoGrpc.UserInfoBlockingStub userInfoStub;

    public long validateUser(@NotNull @Valid UserBasicSignInDto userBasicSignInDto) {
        var passwordResponse = userAuthorizationStub.checkPassword(CheckPasswordRequest.newBuilder()
                .setEmail(userBasicSignInDto.email())
                .setPassword(userBasicSignInDto.password())
                .build());

        if (!passwordResponse.getCorrect())
            throw new UserInfoMismatchException();

        var userResponse = userInfoStub.getUserInfo(BasicUserInfoRequest.newBuilder()
                .setEmail(userBasicSignInDto.email())
                .build());

        return userResponse.getId();
    }

}
