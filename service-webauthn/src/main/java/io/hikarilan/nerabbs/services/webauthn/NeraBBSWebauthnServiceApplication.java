package io.hikarilan.nerabbs.services.webauthn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class NeraBBSWebauthnServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeraBBSWebauthnServiceApplication.class, args);
    }

}
