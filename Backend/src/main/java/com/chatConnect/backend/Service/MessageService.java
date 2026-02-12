package com.chatConnect.backend.Service;

import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.GroupMessage;
import com.chatConnect.backend.Repo.ChatMessageRepo;
import com.chatConnect.backend.Repo.GroupMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    ChatMessageRepo chatMessageRepo;

    @Autowired
    GroupMessageRepo groupMessageRepo;

    public String getMessageContentByMsgId(Long msgId,String type){


        if(type.equals("direct")){
            Optional<ChatMessage> chatMessage=chatMessageRepo.findById(msgId);
            if(chatMessage.isPresent()){
                return chatMessage.get().getContent();
            }
        }else{
            Optional<GroupMessage> groupMessage=groupMessageRepo.findById(msgId);
            if(groupMessage.isPresent()){
                return groupMessage.get().getContent();
            }

        }
        return "";

    }

    public String getTextLanguage(long msgId, String type) {

        if(type.equals("direct")){
            Optional<ChatMessage> chatMessage=chatMessageRepo.findById(msgId);
            if(chatMessage.isPresent()){
                return chatMessage.get().getDetectedLanguage();
            }
        }else{
            Optional<GroupMessage> groupMessage=groupMessageRepo.findById(msgId);
            if(groupMessage.isPresent()){
                return groupMessage.get().getDetectedLanguage();
            }

        }
        return "en"; //default language
    }
}
