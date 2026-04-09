package com.kiruthika.chatapp.ws_service.controller;

import com.kiruthika.chatapp.ws_service.dto.PrivateMessageRequestDto;
import com.kiruthika.chatapp.ws_service.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/private")

    public void sendMessage(Principal principal, PrivateMessageRequestDto dto){

        String username=principal.getName();
        messageService.sendPrivateMessage(username,dto);


    }

    @MessageMapping("/ready")
    public void handleReady(Principal principal){

        String username=principal.getName();
        messageService.handleReady(username);


    }
}
