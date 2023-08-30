package io.hikarilan.nerabbs.services.userprofile.service;

import com.google.protobuf.ByteString;
import io.hikarilan.nerabbs.lib.services.oss.grpc.StorageGrpc;
import io.hikarilan.nerabbs.lib.services.oss.grpc.UploadFileRequest;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AvatarService {

    @GrpcClient("nerabbs-service-oss")
    private StorageGrpc.StorageBlockingStub storageStub;

    public String uploadAvatar(long userID, MultipartFile avatar) throws IOException {
        return "/storages/" + storageStub.uploadFile(UploadFileRequest.newBuilder()
                .setUserID(userID)
                .setFile(io.hikarilan.nerabbs.lib.services.oss.grpc.MultipartFile.newBuilder()
                        .setName(avatar.getName())
                        .setOriginalFileName(avatar.getOriginalFilename())
                        .setContentType(avatar.getContentType())
                        .setBytes(ByteString.copyFrom(avatar.getBytes()))
                        .build())
                .build()).getObject();
    }

}
