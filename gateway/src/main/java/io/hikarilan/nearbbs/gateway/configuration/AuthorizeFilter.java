package io.hikarilan.nearbbs.gateway.configuration;

import cn.dev33.satoken.stp.StpUtil;
import io.hikarilan.nerabbs.common.BizConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                .header(BizConstants.USER_ID_HEADER, StpUtil.getLoginId(BizConstants.USER_ID_UNAUTHORIZED).toString())
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }
}
