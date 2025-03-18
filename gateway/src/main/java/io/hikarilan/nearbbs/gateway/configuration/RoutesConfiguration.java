package io.hikarilan.nearbbs.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfiguration {

    @Value("${nerabbs.frontend.url}")
    private String frontendUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("service-oss", r -> r.path("/api/storages/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://nerabbs-service-oss"))
                .route("service-user-profile", r -> r.path("/api/users/profile/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://nerabbs-service-user-profile"))
                .route("service-user", r -> r.path("/api/users/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://nerabbs-service-user"))
                .route("service-auth", r -> r.path("/api/authorization/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://nerabbs-service-auth"))
                .route("service-post", r -> r.path("/api/posts/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://nerabbs-service-post"))
                .route("service-webauthn", r -> r.path("/api/webauthn/passkey/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://nerabbs-service-webauthn"))
                .route("service-search", r -> r.path("/api/search/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://nerabbs-service-search"))
                .route("frontend", r -> r.path("/**")
                        .uri(frontendUrl))
                .build();
    }

}
