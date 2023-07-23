package io.hikarilan.nerabbs.services.auth.service;

import cn.dev33.satoken.secure.BCrypt;
import io.hikarilan.nerabbs.lib.services.user.grpc.FullUserInfoRequest;
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
    private UserInfoGrpc.UserInfoBlockingStub userInfoStub;

    public long validateUser(@NotNull @Valid UserBasicSignInDto userBasicSignInDto) {
        var info = userInfoStub.getFullUserInfo(FullUserInfoRequest.newBuilder()
                .setEmail(userBasicSignInDto.email())
                .build());

        if (info == null)
            throw new UserInfoMismatchException();

        if (!BCrypt.checkpw(userBasicSignInDto.password(), info.getPassword()))
            throw new UserInfoMismatchException();

        return info.getId();
    }

}
