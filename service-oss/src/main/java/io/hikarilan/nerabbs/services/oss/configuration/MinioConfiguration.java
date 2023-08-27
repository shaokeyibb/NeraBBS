package io.hikarilan.nerabbs.services.oss.configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class MinioConfiguration {

    private final ConfigProperties configProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(configProperties.getEndpoint())
                .credentials(configProperties.getUser(), configProperties.getPassword())
                .build();
    }

}
