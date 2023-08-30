package io.hikarilan.nerabbs.services.oss.grpc;

import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.oss.grpc.StorageGrpc;
import io.hikarilan.nerabbs.lib.services.oss.grpc.UploadFileRequest;
import io.hikarilan.nerabbs.lib.services.oss.grpc.UploadFileResponse;
import io.hikarilan.nerabbs.services.oss.data.pojo.CustomMultipartFile;
import io.hikarilan.nerabbs.services.oss.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcStorageService extends StorageGrpc.StorageImplBase {

    private final StorageService storageService;

    @SneakyThrows
    @Override
    public void uploadFile(UploadFileRequest request, StreamObserver<UploadFileResponse> responseObserver) {
        var resp = storageService.uploadFile(request.getUserID(),
                new CustomMultipartFile("file",
                        request.getFile().getOriginalFileName(),
                        request.getFile().getContentType(),
                        request.getFile().getBytes().toByteArray()
                )
        );

        responseObserver.onNext(UploadFileResponse.newBuilder()
                .setObject(resp.object())
                .build());
        responseObserver.onCompleted();
    }

}
