package io.hikarilan.nerabbs.services.oss.service;

import io.hikarilan.nerabbs.services.oss.configuration.ConfigProperties;
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

@RequiredArgsConstructor
@Service
public class StorageService {

    private final ConfigProperties configProperties;
    private final MinioClient minioClient;

    public ObjectWriteResponse uploadFile(long userID, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (var stream = file.getInputStream()) {
            return minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configProperties.getBucket())
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
                .bucket(configProperties.getBucket())
                .object(object)
                .build());
    }

    public StatObjectResponse getFileStat(String object) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(configProperties.getBucket())
                .object(object)
                .build());
    }

}