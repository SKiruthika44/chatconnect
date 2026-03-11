package com.chatConnect.backend.Controller;

import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.ChatMessageRepo;

import com.chatConnect.backend.Repo.UserRepo;
import com.chatConnect.backend.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController

public class ChatController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ChatMessageRepo chatMessageRepo;


    @Autowired
    private ChatService chatService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }









    @GetMapping("/unreadCounts")
    public ResponseEntity<Map<String,Integer>>  getUnReadCounts(@AuthenticationPrincipal UserPrincipal userPrincipal){

        String username=userPrincipal.getUsername();
        return chatService.getUnReadCounts(username);
    }

    @PutMapping("/makeRead/{receiver}")
    public ResponseEntity<Void> markMessagesRead(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable String receiver){

        String username=userPrincipal.getUsername();
        return chatService.markMessagesRead(username,receiver);
    }


    @PutMapping("/updateMessageRead/{msgId}")
    public ResponseEntity<Void> markMessageRead(@AuthenticationPrincipal UserPrincipal user,@PathVariable int msgId){
        String username=user.getUsername();
        return chatService.markMessageRead(username,msgId);
    }






    @GetMapping("/messages/{otherUsername}")
    public ResponseEntity<List<ChatMessageResponseDTO>> getAllMessagesBetweenSenderAndReceiver(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String otherUsername) {
        return chatService.getAllMessagesBetweenSenderAndReceiver(userPrincipal, otherUsername);
    }




    }
