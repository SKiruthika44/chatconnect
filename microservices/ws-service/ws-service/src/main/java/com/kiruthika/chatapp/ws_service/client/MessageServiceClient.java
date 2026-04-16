package com.kiruthika.chatapp.ws_service.client;

import com.kiruthika.chatapp.ws_service.dto.GroupMessageRequestDto;
import com.kiruthika.chatapp.ws_service.dto.GroupResponseDto;
import com.kiruthika.chatapp.ws_service.dto.PrivateMessageRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class MessageServiceClient {


    private final RestTemplate restTemplate=new RestTemplate();
    public void sendDirectMessage(String username, PrivateMessageRequestDto dto) {
        String url="http://localhost:8083/message/direct/"+username;
        restTemplate.postForObject(url,dto,Void.class);


    }

    public void deliverDirectMessage(String username) {

        String url="http://localhost:8083/message/direct/deliver/"+username;
        restTemplate.put(url,  null);




    }

    public void sendGroupMessage(String username, GroupMessageRequestDto dto) {
        String url="http://localhost:8083/message/group/"+username;
        restTemplate.postForObject(url,dto,Void.class);

    }

    public List<GroupResponseDto> sendUserGroups(String username) {

        String url="http://localhost:8083/group/all-groups/"+username;

        ResponseEntity<List<GroupResponseDto>> response=restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupResponseDto>>() {
        });

        return response.getBody();

    }

    public void deliverGroupMessages(String username) {
        String url="http://localhost:8083/message/group/deliver/"+username;
        restTemplate.put(url,  null);
    }
}
