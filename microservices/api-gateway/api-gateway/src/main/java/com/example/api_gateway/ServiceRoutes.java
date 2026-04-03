package com.example.api_gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
//import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.ws;
@Configuration
public class ServiceRoutes {



    @Bean
    public RouterFunction<ServerResponse> orderRoute(){
        return route("ORDER-SERVICE")
                .route(RequestPredicates.path("/orders/**"),http())
                .filter(lb("ORDER-SERVICE"))
                //.before(uri("http://localhost:8000"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRoute(){
        return route("user-service")
                .route(RequestPredicates.path("/api/**"),http())
                .filter(lb("user-service"))
                //.before(uri("http://localhost:8000"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> websocketRoute(){
        return route("ws-service")
                .route(RequestPredicates.path("/ws/**"),http())
                .filter(lb("ws" +
                        "-service"))
                .before(request -> {
            System.out.println("Handshake/Upgrade request at: " + request.path());
            return request;
        })
                .build();
    }






}
