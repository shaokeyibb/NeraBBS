package io.hikarilan.nerabbs.services.user.grpc;

import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.user.grpc.RegisterUserRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.RegisterUserResponse;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserRegistrationGrpc;
import io.hikarilan.nerabbs.services.user.data.dto.UserBasicRegistrationDto;
import io.hikarilan.nerabbs.services.user.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcUserRegistrationService extends UserRegistrationGrpc.UserRegistrationImplBase {

    private final UserRegistrationService userRegistrationService;

    @Override
    public void registerUser(RegisterUserRequest request, StreamObserver<RegisterUserResponse> responseObserver) {
        var id = userRegistrationService.registerUser(new UserBasicRegistrationDto(request.getEmail(), request.getPassword()));

        var resp = RegisterUserResponse.newBuilder()
                .setId(id)
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
