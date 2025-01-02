package io.hikarilan.nearbbs.gateway.configuration;

import cn.dev33.satoken.stp.StpUtil;
import io.hikarilan.nerabbs.common.BizConstants;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements WebFilter {

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                .header(BizConstants.USER_ID_HEADER, StpUtil.getLoginId(BizConstants.USER_ID_UNAUTHORIZED).toString())
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }
}
