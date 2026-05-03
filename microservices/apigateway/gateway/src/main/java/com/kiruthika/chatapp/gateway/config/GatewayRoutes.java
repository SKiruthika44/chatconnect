package com.kiruthika.chatapp.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class GatewayRoutes {


        @Bean
        public RouteLocator customRouteLocator(
                RouteLocatorBuilder builder) {

            return builder.routes()

                    .route("ws-service", r -> r
                            .path("/ws/**")
                            .uri("lb://ws-service")
                    )

                    .build();

        }
    @Bean
    public RouteLocator customUserRouteLocator(
            RouteLocatorBuilder builder) {

        return builder.routes()

                .route("user-service", r -> r
                        .path("/api/**","/uploads/**")
                        .uri("lb://user-service")
                )

                .build();

    }

    @Bean
    public RouteLocator customMessageRouteLocator(
            RouteLocatorBuilder builder) {

        return builder.routes()

                .route("messaging-service", r -> r
                        .path("/message/**","/group/**")
                        .uri("lb://messaging-service")
                )

                .build();

    }


}
