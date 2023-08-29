package io.hikarilan.nerabbs.services.oss.service;

import io.hikarilan.nerabbs.services.oss.configuration.FileUploadConfigProperties;
import io.hikarilan.nerabbs.services.oss.configuration.MinioConfigProperties;
import io.hikarilan.nerabbs.services.oss.data.pojo.FileAndState;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final FileUploadConfigProperties fileUploadConfigProperties;
    private final MinioConfigProperties minioConfigProperties;
    private final MinioClient minioClient;

    public ObjectWriteResponse uploadFile(long userID, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!fileUploadConfigProperties.getAllowedFileMimeTypes().contains(file.getContentType())) {
            throw new IllegalArgumentException("File type not allowed");
        }

        try (var stream = file.getInputStream()) {
            return minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfigProperties.getBucket())
                    .object(UUID.randomUUID().toString())
                    .stream(stream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .userMetadata(new HashMap<>() {{
                        put("UserID", String.valueOf(userID));
                        put("FileName", file.getOriginalFilename());
                    }})
                    .build());
        }
    }

    public GetObjectResponse getFile(String object) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioConfigProperties.getBucket())
                .object(object)
                .build());
    }

    public StatObjectResponse getFileStat(String object) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(minioConfigProperties.getBucket())
                .object(object)
                .build());
    }

    public FileAndState getFileAndState(String object) {
        var file = CompletableFuture.supplyAsync(() -> {
            try {
                return getFile(object);
            } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                     NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                     InternalException e) {
                throw new RuntimeException(e);
            }
        });

        var stat = CompletableFuture.supplyAsync(() -> {
            try {
                return getFileStat(object);
            } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                     NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                     InternalException e) {
                throw new RuntimeException(e);
            }
        });

        var all = CompletableFuture.allOf(file, stat);

        all.join();

        return new FileAndState(file.join(), stat.join());
    }

}