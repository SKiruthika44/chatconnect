package com.kiruthika.chatapp.messaging_service.controller;

import com.kiruthika.chatapp.messaging_service.dto.PrivateMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.service.DirectMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/message/direct")
public class DirectMessageController {

    private final DirectMessageService directMessageService;

    @PostMapping("/{sender}")

    public void sendDirectMessage(@PathVariable String sender,@RequestBody PrivateMessageRequestDto dto){
        directMessageService.sendDirectMessage(sender,dto);
    }
    
}
