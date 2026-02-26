package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.PrivateMessageEditedEvent;
import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.ChatMessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component

public class EditListener {

    @Autowired

    private SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void notifyPrivateMessageEdited(PrivateMessageEditedEvent privateMessageEditedEvent){
        ChatMessage chatMessage=privateMessageEditedEvent.getChatMessage();
        ChatMessageResponseDTO chatMessageResponseDTO=new ChatMessageResponseDTO(chatMessage.getId(), chatMessage.getContent(),chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");
        if(chatMessage.isDelivered()){
            chatMessageResponseDTO.setStatus("DELIVERED");
        }
        if(chatMessage.isRead()){
            chatMessageResponseDTO.setStatus("READ");
        }
        if(chatMessage.isDeletedForEveryOne()){
            chatMessageResponseDTO.setDeletedForEveryone(true);
        }
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/message/edit",chatMessageResponseDTO);

    }
}
