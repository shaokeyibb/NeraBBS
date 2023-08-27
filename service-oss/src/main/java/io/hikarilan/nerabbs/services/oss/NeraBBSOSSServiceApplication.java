package io.hikarilan.nerabbs.services.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NeraBBSOSSServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeraBBSOSSServiceApplication.class, args);
    }

}
