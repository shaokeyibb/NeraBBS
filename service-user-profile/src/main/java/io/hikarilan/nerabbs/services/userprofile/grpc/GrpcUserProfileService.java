package io.hikarilan.nerabbs.services.userprofile.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.userprofile.grpc.UpdateUserProfileRequest;
import io.hikarilan.nerabbs.lib.services.userprofile.grpc.UserProfileGrpc;
import io.hikarilan.nerabbs.services.userprofile.data.pojo.CustomMultipartFile;
import io.hikarilan.nerabbs.services.userprofile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcUserProfileService extends UserProfileGrpc.UserProfileImplBase {

    private final UserProfileService userProfileService;

    @Override
    public void updateUserProfile(UpdateUserProfileRequest request, StreamObserver<Empty> responseObserver) {
        try {
            userProfileService.updateUserProfile(request.getId(), request.getUsername(), new CustomMultipartFile("file",
                    request.getAvatar().getOriginalFileName(),
                    request.getAvatar().getContentType(),
                    request.getAvatar().getBytes().toByteArray()
            ), request.getSignature());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (IOException e) {
            responseObserver.onError(e);
        }
    }
}
