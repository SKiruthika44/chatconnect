package com.kiruthika.chatapp.ws_service.controller;

import com.kiruthika.chatapp.ws_service.Config.PresenceEventListener;
import com.kiruthika.chatapp.ws_service.dto.PrivateMessageRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@AllArgsConstructor
public class WebsocketController {
    private final PresenceEventListener presenceEventListener;

    @GetMapping("/onlineusers")
    public Set<String> getOnlineUsers(){
        return presenceEventListener.getOnlineUsers();
    }




}
