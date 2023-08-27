package io.hikarilan.nerabbs.services.oss.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class ConfigProperties {

    @NotBlank
    private String endpoint;
    @NotBlank
    private String user;
    @NotBlank
    private String password;

    @NotBlank
    @Value("${nerabbs.oss.minio.bucket:nerabbs}")
    private String bucket;

}
