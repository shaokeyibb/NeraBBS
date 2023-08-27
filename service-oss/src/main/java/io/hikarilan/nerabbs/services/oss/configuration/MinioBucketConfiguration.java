package io.hikarilan.nerabbs.services.oss.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Configuration
public class MinioBucketConfiguration {

    private final ConfigProperties configProperties;
    private final MinioClient minioClient;

    @PostConstruct
    public void initializeBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(configProperties.getBucket()).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(configProperties.getBucket()).build());
        }
    }

}
