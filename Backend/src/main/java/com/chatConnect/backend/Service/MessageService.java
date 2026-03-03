package com.chatConnect.backend.Service;

import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.ChatMessageRepo;
import com.chatConnect.backend.Repo.GroupMessageRepo;
import com.chatConnect.backend.Repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {
    @Autowired

    private ChatService chatService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    ChatMessageRepo chatMessageRepo;

    @Autowired
    GroupMessageRepo groupMessageRepo;

    public String getMessageContentByMsgId(Long msgId){

        Message message=messageRepo.findByMsgId(msgId);
        return message.getContent();



    }

    public String getTextLanguage(long msgId) {
        Message message=messageRepo.findByMsgId(msgId);


        return message.getDetectedLanguage();

    }

    public void sendDirectMessage(String username, ChatMessageDTO msg) {
        chatService.sendMessage(username,msg);
    }

    public void sendGroupMessage(String username, GroupMessageRequestDTO groupMessage) {
        groupService.sendGroupMessage(username,groupMessage);
    }

    public void markMessageRead(String username, long msgId) {
        Message message=messageRepo.findByMsgId(msgId);
        if(message instanceof ChatMessage){
            chatService.markMessageRead(username,msgId);
        }
        else if(message instanceof  GroupMessage){
            groupService.markGroupMessageRead(username,msgId);
        }



    }

    public ResponseEntity<Void> deleteMessage(String username, long msgId, String scope) {
        Message message=messageRepo.findByMsgId(msgId);
        if(message instanceof ChatMessage){
            return chatService.deleteMessage(username,msgId,scope);
        }

        return groupService.deleteMessage(username,msgId,scope);

    }

    public ResponseEntity<Void> reactEmoji(String username, long msgId, String emoji) {
        Message message=messageRepo.findByMsgId(msgId);
        if(message instanceof ChatMessage){
            return chatService.reactEmoji(username,msgId,emoji);
        }

        return groupService.reactEmoji(username,msgId,emoji);
    }

    public ResponseEntity<Void> editMessage(String username, long msgId, String content) {
        Message message=messageRepo.findByMsgId(msgId);
        if(message instanceof ChatMessage){
            return chatService.editMessage(username,msgId,content);
        }

        return groupService.editMessage(username,msgId,content);

    }
}
