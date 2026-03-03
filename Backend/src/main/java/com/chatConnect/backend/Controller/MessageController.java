package com.chatConnect.backend.Controller;

import com.chatConnect.backend.Modal.ChatMessageDTO;
import com.chatConnect.backend.Modal.GroupMessageRequestDTO;
import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller

public class MessageController {
    @Autowired
    private MessageService messageService;

    @MessageMapping("/private")
    public void sendMessage(UserPrincipal userPrincipal, ChatMessageDTO msg){
        String username=userPrincipal.getUsername();
        messageService.sendDirectMessage(username,msg);


    }

    @MessageMapping("/group")
    public void sendGroupMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GroupMessageRequestDTO groupMessage){
        messageService.sendGroupMessage(userPrincipal.getUsername(),groupMessage);
    }




    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,  @RequestParam long msgId, @RequestParam String scope){
        return messageService.deleteMessage(userPrincipal.getUsername(),msgId,scope);
    }

    @PutMapping("/message/emoji")

    public ResponseEntity<Void> reactEmoji(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam long msgId, String emoji){
        System.out.println("request came");
        return messageService.reactEmoji(userPrincipal.getUsername(),msgId,emoji);
    }

    @PutMapping("/message/edit/{msgId}")
    public ResponseEntity<Void> editMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long msgId, @RequestParam String content){
        System.out.println("in edit controller");
        return messageService.editMessage(userPrincipal.getUsername(),msgId,content);
    }





}
