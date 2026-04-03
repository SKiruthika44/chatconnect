package com.example.api_gateway;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Component

public class JwtAuthenticationFilter extends
        OncePerRequestFilter {
    @Autowired

    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("path="+path);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Skip authentication endpoints
        if (path.startsWith("/api/auth") || path.startsWith("/ws")) {
            System.out.println("Gateway: Passing /ws/info handshake through.");
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        System.out.println("header"+header);
        if (header == null || !header.startsWith("Bearer ")) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = header.substring(7);

        if (!jwtService.isTokenValid(token)) {

            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.getWriter().write("Invalid or expired token");
            sendUnauthorized(response, "Missing or invalid Authorization header");

            return;
        }

        // Extract username
        String username = jwtService.extractUsername(token);
        System.out.println("username"+username);
        // Send username to downstream services
        //request.setAttribute("X-User", username);

        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                if ("X-User".equalsIgnoreCase(name)) {
                    return username;
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if ("X-User".equalsIgnoreCase(name)) {
                    return Collections.enumeration(Collections.singletonList(username));
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                List<String> names = Collections.list(super.getHeaderNames());
                names.add("X-User");
                return Collections.enumeration(names);
            }
        };
        filterChain.doFilter(wrappedRequest, response);


    }

    private void sendUnauthorized(HttpServletResponse response, String message)
            throws IOException {

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }

}
