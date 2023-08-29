package io.hikarilan.nerabbs.services.oss.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "nerabbs.oss.upload")
public class FileUploadConfigProperties {

    private Set<String> allowedFileMimeTypes;

}
