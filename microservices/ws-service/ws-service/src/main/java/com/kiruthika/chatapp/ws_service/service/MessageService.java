package com.kiruthika.chatapp.ws_service.service;

import com.kiruthika.chatapp.ws_service.Config.PresenceEventListener;
import com.kiruthika.chatapp.ws_service.client.MessageServiceClient;
import com.kiruthika.chatapp.ws_service.dto.PrivateMessageRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageServiceClient messageServiceClient;

    private final PresenceEventListener presenceEventListener;

    public void sendPrivateMessage(String username, PrivateMessageRequestDto dto) {
        messageServiceClient.sendDirectMessage(username,dto);

    }

    public void handleReady(String username) {
        broadcastOnlineUsersPrivate(username);
        deliverDirectMessages(username);

    }

    private void deliverDirectMessages(String username) {
        messageServiceClient.deliverDirectMessage(username);
    }

    private void broadcastOnlineUsersPrivate(String username) {
        presenceEventListener.broadcastOnlineUsersPrivate(username);
    }
}
