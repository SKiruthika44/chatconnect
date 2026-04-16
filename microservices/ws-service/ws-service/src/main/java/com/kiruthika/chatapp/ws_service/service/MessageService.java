package com.kiruthika.chatapp.ws_service.service;

import com.kiruthika.chatapp.ws_service.Config.PresenceEventListener;
import com.kiruthika.chatapp.ws_service.client.MessageServiceClient;
import com.kiruthika.chatapp.ws_service.dto.GroupMessageRequestDto;
import com.kiruthika.chatapp.ws_service.dto.GroupResponseDto;
import com.kiruthika.chatapp.ws_service.dto.PrivateMessageRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageServiceClient messageServiceClient;

    private final PresenceEventListener presenceEventListener;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendDirectMessage(String username, PrivateMessageRequestDto dto) {
        messageServiceClient.sendDirectMessage(username,dto);

    }

    public void handleReady(String username) {

        broadcastOnlineUsersPrivate(username);
        deliverDirectMessages(username);

        sendUserGroups(username);
        deliverGroupMessages(username);


    }

    private void deliverGroupMessages(String username) {
        messageServiceClient.deliverGroupMessages(username);
    }

    private void sendUserGroups(String username) {

        List<GroupResponseDto> dtos=messageServiceClient.sendUserGroups(username);

        simpMessagingTemplate.convertAndSendToUser(username,"/queue/groupInfo",dtos);


    }

    private void deliverDirectMessages(String username) {
        messageServiceClient.deliverDirectMessage(username);
    }

    private void broadcastOnlineUsersPrivate(String username) {
        presenceEventListener.broadcastOnlineUsersPrivate(username);
    }

    public void sendGroupMessage(String username, GroupMessageRequestDto dto) {
        messageServiceClient.sendGroupMessage(username,dto);
    }
}
