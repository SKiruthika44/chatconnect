package com.kiruthika.chatapp.ws_service.service;

import com.kiruthika.chatapp.ws_service.client.MessageServiceClient;
import com.kiruthika.chatapp.ws_service.dto.PrivateMessageRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageServiceClient messageServiceClient;

    public void sendPrivateMessage(String username, PrivateMessageRequestDto dto) {
        messageServiceClient.sendDirectMessage(username,dto);

    }
}
