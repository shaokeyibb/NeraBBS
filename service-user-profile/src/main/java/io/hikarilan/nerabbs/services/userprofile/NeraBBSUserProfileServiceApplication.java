package io.hikarilan.nerabbs.services.userprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class NeraBBSUserProfileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeraBBSUserProfileServiceApplication.class, args);
    }

}
