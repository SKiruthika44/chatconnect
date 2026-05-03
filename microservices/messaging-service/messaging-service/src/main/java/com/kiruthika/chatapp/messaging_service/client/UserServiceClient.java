package com.kiruthika.chatapp.messaging_service.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class UserServiceClient{
    private final RestTemplate restTemplate=new RestTemplate();

    public Long getUserIdByUsername(String username){
        String url="http://localhost:8082/api/user/id/"+username;
        return restTemplate.getForObject(url,Long.class);
    }


    public String getUsernameById(Long userId) {
        String url="http://localhost:8082/api/user/username/"+userId;
        return restTemplate.getForObject(url,String.class);

    }

    public String getUserPreferredLanguage(Long userId){
        String url="http://localhost:8082/api/user/preferred-lang/"+userId;
        return restTemplate.getForObject(url,String.class);

    }
}
