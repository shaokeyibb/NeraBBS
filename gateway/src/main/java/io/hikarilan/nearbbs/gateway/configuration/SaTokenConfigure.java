package io.hikarilan.nearbbs.gateway.configuration;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Configuration
public class SaTokenConfigure {

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/api/**")
                .addExclude("/**")
                .setAuth(obj -> {
                    SaRouter.match("/**", "/api/authorization/**", r -> StpUtil.checkLogin());
                })
                .setError(e -> {
                    if (e instanceof NotLoginException) {
                        ServerWebExchange exchange = SaReactorSyncHolder.getContext();
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return "401 Unauthorized";
                    }
                    log.error("Unhandled error during SaReactorFilter", e);
                    return e.toString();
                })
                .setBeforeAuth(obj -> {
                    SaRouter.match(SaHttpMethod.OPTIONS).back();
                });
    }

}
