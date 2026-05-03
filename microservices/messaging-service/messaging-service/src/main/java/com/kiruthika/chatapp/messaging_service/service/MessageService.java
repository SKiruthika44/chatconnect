package com.kiruthika.chatapp.messaging_service.service;


import com.kiruthika.chatapp.messaging_service.api.TranslationApi;
import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.modal.DirectMessage;
import com.kiruthika.chatapp.messaging_service.modal.Message;
import com.kiruthika.chatapp.messaging_service.repo.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepo messageRepo;
    private DirectMessageService directMessageService;
    private GroupMessageService groupMessageService;
    private UserServiceClient userServiceClient;
    private TranslationApi translationApi;
    public ResponseEntity<Void> editMessage(String username, long msgId, String content) {
        Message message=messageRepo.findByMsgId(msgId);
        if(message instanceof DirectMessage){
            return directMessageService.editMessage(username,msgId,content);
        }

        return groupMessageService.editMessage(username,msgId,content);

    }

    public ResponseEntity<Void> deleteMessage(String username, long msgId, String scope) {
        Message message=messageRepo.findByMsgId(msgId);
        if(message instanceof DirectMessage){
            return directMessageService.deleteMessage(username,msgId,scope);
        }

        return groupMessageService.deleteMessage(username,msgId,scope);

    }

    public ResponseEntity<Void> reactMessage(String username, long msgId, String emoji) {
        Message message=messageRepo.findByMsgId(msgId);
        if(message instanceof DirectMessage){
            return directMessageService.reactMessage(username,msgId,emoji);
        }

        return groupMessageService.reactMessage(username,msgId,emoji);
    }

    public ResponseEntity<String> translateText(String username, long msgId) {
        Long userId=userServiceClient.getUserIdByUsername(username);
        String targetLang=userServiceClient.getUserPreferredLanguage(userId);
        String sourceLang=messageRepo.findByMsgId(msgId).getDetectedLanguage();
        String text=messageRepo.findByMsgId(msgId).getContent();
        String translatedText=translationApi.translate(text,sourceLang,targetLang);
        return ResponseEntity.ok(translatedText);

    }
}
