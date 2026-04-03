package com.kiruthika.chatapp.messaging_service.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service

public class WsServiceClient {

    private final RestTemplate restTemplate=new RestTemplate();

    public Set<String> getOnlineUsers() {
        String url="http://localhost:8085/onlineusers";

        ResponseEntity<Set<String>> response=restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Set<String>>() {
        });
        return response.getBody();
    }
}
