package com.kiruthika.chatapp.messaging_service.controller;


import com.kiruthika.chatapp.messaging_service.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @PutMapping("/edit/{msgId}")
    public ResponseEntity<Void> editMessage(@RequestHeader("X-User") String username, @PathVariable long msgId, @RequestParam String content){

        return messageService.editMessage(username,msgId,content);
    }

    @DeleteMapping("/delete/{msgId}")
    public ResponseEntity<Void> deleteMessage(@RequestHeader("X-User") String username, @PathVariable long msgId, @RequestParam String scope){

        return messageService.deleteMessage(username,msgId,scope);
    }

    @PutMapping("/react/{msgId}")
    public ResponseEntity<Void> reactMessage(@RequestHeader("X-User") String username, @PathVariable long msgId, @RequestParam String emoji){

        return messageService.reactMessage(username,msgId,emoji);
    }

    @GetMapping("/translate/{msgId}")
    public ResponseEntity<String> getTranslatedText(@RequestHeader("X-User") String username, @PathVariable long msgId){
        return messageService.translateText(username,msgId);
    }








}
