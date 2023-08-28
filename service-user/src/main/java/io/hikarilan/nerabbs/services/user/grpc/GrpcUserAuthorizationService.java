package io.hikarilan.nerabbs.services.user.grpc;

import cn.hutool.crypto.digest.BCrypt;
import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.user.grpc.CheckPasswordRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.CheckPasswordResponse;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserAuthorizationGrpc;
import io.hikarilan.nerabbs.services.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcUserAuthorizationService extends UserAuthorizationGrpc.UserAuthorizationImplBase {

    private final UserRepository userRepository;

    @Override
    public void checkPassword(CheckPasswordRequest request, StreamObserver<CheckPasswordResponse> responseObserver) {
        var exists = switch (request.getIdentifyCase()) {
            case ID -> userRepository.existsById(request.getId());
            case EMAIL -> userRepository.existsByEmail(request.getEmail());
            case IDENTIFY_NOT_SET -> throw new IllegalArgumentException("Identify not set");
        };

        if (!exists) {
            responseObserver.onNext(CheckPasswordResponse.newBuilder()
                    .setCorrect(false)
                    .build());
            responseObserver.onCompleted();
            return;
        }

        var user = switch (request.getIdentifyCase()) {
            case ID -> userRepository.findById(request.getId()).orElseThrow();
            case EMAIL -> userRepository.findByEmail(request.getEmail()).orElseThrow();
            case IDENTIFY_NOT_SET -> throw new IllegalArgumentException("Identify not set");
        };

        var resp = CheckPasswordResponse.newBuilder()
                .setCorrect(BCrypt.checkpw(request.getPassword(), user.getPassword()))
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
