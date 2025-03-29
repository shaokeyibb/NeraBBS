package io.hikarilan.nerabbs.services.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NeraBBSSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeraBBSSearchServiceApplication.class, args);
    }

}
