package io.hikarilan.nerabbs.services.comment.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.comment.grpc.CommentGrpc;
import io.hikarilan.nerabbs.lib.services.comment.grpc.DeleteAllCommentsRequest;
import io.hikarilan.nerabbs.services.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcCommentService extends CommentGrpc.CommentImplBase {

    private final CommentService commentService;

    @Override
    public void deleteAllComments(DeleteAllCommentsRequest request, StreamObserver<Empty> responseObserver) {
        commentService.deleteAllComments(request.getPostId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
