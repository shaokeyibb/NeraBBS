package io.hikarilan.nerabbs.services.search.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.search.grpc.HitGrpc;
import io.hikarilan.nerabbs.lib.services.search.grpc.HitRequest;
import io.hikarilan.nerabbs.lib.services.search.grpc.ResetRequest;
import io.hikarilan.nerabbs.services.search.service.HitService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcHitService extends HitGrpc.HitImplBase {

    private final HitService hitService;

    @Override
    public void hit(HitRequest request, StreamObserver<Empty> responseObserver) {
        hitService.hit(request.getTopic(), request.getKey());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void reset(ResetRequest request, StreamObserver<Empty> responseObserver) {
        hitService.reset(request.getTopic(), request.getKey());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
