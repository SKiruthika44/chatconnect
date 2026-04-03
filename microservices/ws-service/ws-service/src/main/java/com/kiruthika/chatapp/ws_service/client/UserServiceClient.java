package com.kiruthika.chatapp.ws_service.client;

import com.kiruthika.chatapp.ws_service.dto.LastSeenRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service

public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public void updateLastSeen(String username, LocalDateTime lastseen) {
        String url="http://localhost:8082/api/user/lastseen";
        LastSeenRequestDto dto=new LastSeenRequestDto();
    dto.setLastseen(lastseen);
        dto.setUsername(username);
        restTemplate.put(url,dto);

    }
}
