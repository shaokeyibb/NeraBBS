package io.hikarilan.nerabbs.services.user.grpc;

import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.common.exception.UserNotFoundException;
import io.hikarilan.nerabbs.lib.services.user.grpc.BasicUserInfoRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.BasicUserInfoResponse;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserInfoGrpc;
import io.hikarilan.nerabbs.services.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcUserInfoService extends UserInfoGrpc.UserInfoImplBase {

    private final UserRepository userRepository;

    @Override
    public void getUserInfo(BasicUserInfoRequest request, StreamObserver<BasicUserInfoResponse> responseObserver) {
        var exists = switch (request.getIdentifyCase()) {
            case ID -> userRepository.existsById(request.getId());
            case EMAIL -> userRepository.existsByEmail(request.getEmail());
            case IDENTIFY_NOT_SET -> throw new IllegalArgumentException("Identify not set");
        };

        if (!exists) {
            responseObserver.onError(new UserNotFoundException());
            return;
        }

        var user = switch (request.getIdentifyCase()) {
            case ID -> userRepository.findById(request.getId()).orElseThrow();
            case EMAIL -> userRepository.findByEmail(request.getEmail()).orElseThrow();
            case IDENTIFY_NOT_SET -> throw new IllegalArgumentException("Identify not set");
        };

        var resp = BasicUserInfoResponse.newBuilder()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setCreateAt(user.getCreateAt().toInstant().toString())
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
