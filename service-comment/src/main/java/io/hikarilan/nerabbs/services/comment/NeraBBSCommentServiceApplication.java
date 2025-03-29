package io.hikarilan.nerabbs.services.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class NeraBBSCommentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeraBBSCommentServiceApplication.class, args);
    }

}
