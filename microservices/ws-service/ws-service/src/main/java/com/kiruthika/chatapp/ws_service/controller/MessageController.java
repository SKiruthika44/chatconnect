package com.kiruthika.chatapp.ws_service.controller;

import com.kiruthika.chatapp.ws_service.dto.GroupMessageRequestDto;
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
    public void sendDirectMessage(Principal principal, PrivateMessageRequestDto dto){
        String username=principal.getName();
        messageService.sendDirectMessage(username,dto);
    }

    @MessageMapping("/group")
    public void sendGroupMessage(Principal principal, GroupMessageRequestDto dto){
        String username=principal.getName();
        messageService.sendGroupMessage(username,dto);
    }








    @MessageMapping("/ready")
    public void handleReady(Principal principal){
        String username=principal.getName();
        messageService.handleReady(username);
    }
}
