package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.PrivateMessageDeletedEvent;
import com.chatConnect.backend.Event.MessageDeletedForMeEvent;
import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.ChatMessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component

public class PrivateMessageDeletedEventListener {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @EventListener

    public void notifyDeletion(PrivateMessageDeletedEvent privateMessageDeletedEvent){
        ChatMessage chatMessage=privateMessageDeletedEvent.getChatMessage();
        ChatMessageResponseDTO chatMessageResponseDTO=new ChatMessageResponseDTO(chatMessage.getId(), null,chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");
        if(chatMessage.isDelivered()){
            chatMessageResponseDTO.setStatus("DELIVERED");
        }
        if(chatMessage.isRead()){
            chatMessageResponseDTO.setStatus("READ");
        }
        chatMessageResponseDTO.setDeletedForEveryone(true);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver().getUsername(),"/queue/delete",chatMessageResponseDTO);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/delete",chatMessageResponseDTO);

    }


}
