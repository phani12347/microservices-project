package com.example.gateway.filter;

import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtAuthFilter
        extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final WebClient client = WebClient.create();

    public JwtAuthFilter() {
        super(Config.class);
    }

    public static class Config {
        // empty (needed by Spring)
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            String auth = exchange.getRequest()
                    .getHeaders()
                    .getFirst("Authorization");

            if (auth == null) {
                exchange.getResponse().setStatusCode(
                        org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return client.get()
                    .uri("http://localhost:5001/auth/validate-token")
                    .header("Authorization", auth)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .flatMap(claims -> {

                        String role = claims.get("role").toString();

                        // ADMIN check for DELETE
                        if (exchange.getRequest().getMethod() == HttpMethod.DELETE
                                && !role.equals("ADMIN")) {

                            exchange.getResponse().setStatusCode(
                                    org.springframework.http.HttpStatus.FORBIDDEN);

                            return exchange.getResponse().setComplete();
                        }

                        // Add user id to header
                        ServerHttpRequest request = exchange.getRequest()
                                .mutate()
                                .header("X-USER-ID", "1")
                                .build();

                        return chain.filter(
                                exchange.mutate()
                                        .request(request)
                                        .build());
                    })
                    .onErrorResume(e -> {

                        exchange.getResponse().setStatusCode(
                                org.springframework.http.HttpStatus.UNAUTHORIZED);

                        return exchange.getResponse().setComplete();
                    });
        };
    }
}
