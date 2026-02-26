package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.MessageDeletedForMeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component

public class MessageDeletedForMeEventListener {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void notifyDeleteForMe(MessageDeletedForMeEvent messageDeletedForMeEvent){
        simpMessagingTemplate.convertAndSendToUser(messageDeletedForMeEvent.getUsername(),"/queue/delete-for-me",messageDeletedForMeEvent.getMsgId());
    }
}
