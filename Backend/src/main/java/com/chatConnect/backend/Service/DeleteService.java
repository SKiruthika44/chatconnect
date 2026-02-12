package com.chatConnect.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteService {
    @Autowired
    ChatService chatService;

    @Autowired
    GroupService groupService;
    public ResponseEntity<Void> deleteMessage(String username, long msgId, String type,String scope) {
        if(type.equals("direct")){
            return chatService.deleteMessage(username,msgId,scope);


        }
        else{
            return groupService.deleteMessage(username,msgId,scope);
        }


    }
}
