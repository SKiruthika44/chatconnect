package com.chatConnect.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service

public class EmojiService {
    @Autowired
    ChatService chatService;

    @Autowired
    GroupService groupService;
    public ResponseEntity<Void> reactEmoji(String username, long msgId, String msgType, String emoji) {
        if(msgType.equals("private")){
            System.out.println("request sent");
            return chatService.reactEmoji(username,msgId,emoji);
        }
        else{
            System.out.println("request came");
           return groupService.reactEmoji(username,msgId,emoji);
        }
    }

    public ResponseEntity<Void> editMessage(String username, long msgId, String type, String content) {
        System.out.println("in edit service");
        if(type.equals("private")){
            return chatService.editMessage(username,msgId,content);
        }
        else{
            return groupService.editMessage(username,msgId,content);
        }
    }
}
