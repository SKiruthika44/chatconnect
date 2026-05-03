package com.kiruthika.chatapp.messaging_service.controller;

import com.kiruthika.chatapp.messaging_service.dto.DirectMessageResponseDto;
import com.kiruthika.chatapp.messaging_service.dto.PrivateMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.service.DirectMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/message/direct")
public class DirectMessageController {

    private final DirectMessageService directMessageService;

    @PostMapping("/{sender}")
    public void sendDirectMessage(@PathVariable String sender,@RequestBody PrivateMessageRequestDto dto){
        directMessageService.sendDirectMessage(sender,dto);
    }

    @PutMapping("/updateMessageRead/{msgId}")
    public ResponseEntity<Void> markMessageRead(@RequestHeader("X-User")String username, @PathVariable Long msgId){

        return directMessageService.markMessageRead(username,msgId);
    }

    @PutMapping("/makeRead/{receiver}")
    public ResponseEntity<Void> markMessagesRead(@RequestHeader("X-User")String username,@PathVariable String receiver){
        return directMessageService.markMessagesRead(username,receiver);
    }

    @GetMapping("/unreadCounts")
    public ResponseEntity<Map<String,Integer>>  getUnReadCounts(@RequestHeader("X-User") String username){
        return directMessageService.getUnReadCounts(username);
    }

    @GetMapping("/all-messages/{otherUsername}")
    public ResponseEntity<List<DirectMessageResponseDto>> getAllMessagesBetweenSenderAndReceiver(@RequestHeader("X-User") String username, @PathVariable String otherUsername) {
        return directMessageService.getAllMessagesBetweenSenderAndReceiver(username, otherUsername);
    }

    @PutMapping("/deliver/{username}")
    public void deliverDirectMessages(@PathVariable String username){
        directMessageService.deliverDirectMessages(username);
    }


    
}
