package com.kiruthika.chatapp.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityFilterChain(
            ServerHttpSecurity http) {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors->{})
                .authorizeExchange(auth -> auth

                        .pathMatchers(HttpMethod.OPTIONS)
                        .permitAll()

                        // public endpoints
                        .pathMatchers("/api/auth/**","/uploads/**")
                        .permitAll()

                        // websocket handled in ws-service
                        .pathMatchers("/ws/**")
                        .permitAll()

                        // everything else requires JWT
                        .anyExchange()
                        .permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())

                .build();
    }
}
