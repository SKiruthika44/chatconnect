package com.kiruthika.chatapp.gateway;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        String method = request.getMethod().name();

        System.out.println("path=" + path);

        // Skip OPTIONS
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }

        // Skip auth and websocket
        if (path.startsWith("/api/auth") || path.startsWith("/ws") || path.startsWith("/uploads/")) {
            System.out.println("Gateway: Passing /ws handshake through.");
            return chain.filter(exchange);
        }

        String header = request.getHeaders().getFirst("Authorization");
        System.out.println("header=" + header);

        if (header == null || !header.startsWith("Bearer ")) {
            return sendUnauthorized(exchange,
                    "Missing or invalid Authorization header");
        }

        String token = header.substring(7);

        if (!jwtService.isTokenValid(token)) {
            return sendUnauthorized(exchange,
                    "Invalid or expired token");
        }

        // Extract username
        String username = jwtService.extractUsername(token);

        System.out.println("username=" + username);

        // Add X-User header
        ServerHttpRequest mutatedRequest =
                request.mutate()
                        .header("X-User", username)
                        .build();

        return chain.filter(
                exchange.mutate()
                        .request(mutatedRequest)
                        .build()
        );
    }

    private Mono<Void> sendUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();

        response.getHeaders().add(
                "Access-Control-Allow-Origin",
                "http://localhost:5173"
        );

        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer =
                response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
