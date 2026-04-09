package com.kiruthika.chatapp.ws_service.client;

import com.kiruthika.chatapp.ws_service.dto.PrivateMessageRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageServiceClient {


    private final RestTemplate restTemplate=new RestTemplate();
    public void sendDirectMessage(String username, PrivateMessageRequestDto dto) {
        String url="http://localhost:8083/message/direct/"+username;
        restTemplate.postForObject(url,dto,Void.class);


    }

    public void deliverDirectMessage(String username) {
        String url="http://localhost:8083/message/direct/deliver/"+username;
        restTemplate.put(url, (Object) null);


    }
}
