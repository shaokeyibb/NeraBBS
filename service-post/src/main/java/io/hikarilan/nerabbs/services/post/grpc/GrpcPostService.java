package io.hikarilan.nerabbs.services.post.grpc;

import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.post.grpc.HasPostRequest;
import io.hikarilan.nerabbs.lib.services.post.grpc.HasPostResponse;
import io.hikarilan.nerabbs.lib.services.post.grpc.PostGrpc;
import io.hikarilan.nerabbs.services.post.service.PostService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcPostService extends PostGrpc.PostImplBase {

    private final PostService postService;

    @Override
    public void hasPost(HasPostRequest request, StreamObserver<HasPostResponse> responseObserver) {
        responseObserver.onNext(HasPostResponse.newBuilder().setHas(postService.hasPost(request.getId())).build());
        responseObserver.onCompleted();
    }
}
