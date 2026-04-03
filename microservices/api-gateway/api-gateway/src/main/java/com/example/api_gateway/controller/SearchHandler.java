package com.example.api_gateway.controller;

import com.example.api_gateway.JwtService;
import com.example.api_gateway.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

@Component

public class SearchHandler {

    @Autowired

    private RestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    /*@GetMapping
    public ServerResponse handleSearch(ServerRequest request){
        String query = request.param("q").orElse("");

        // 1. Get the Authorization header from the incoming request
        String authHeader = request.headers().header(HttpHeaders.AUTHORIZATION)
                .stream().findFirst().orElse("");

        // 2. Extract username using your JwtService
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : "";
        String loggedInUser = jwtService.extractUsername(token);

        // 3. Build headers for the call to User-Service
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User", loggedInUser); // Manually set the header here!
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 4. Use exchange() to send the header

        List<UserResponseDTO> users= restTemplate.exchange( "http://user-service/api/user/search?keyword=" + query,HttpMethod.GET,entity,List.class);

        SearchResponseDTO searchResult = new SearchResponseDTO(response.getBody(), new ArrayList<>());
        return ServerResponse.ok().body(searchResult);
    }*/




}
