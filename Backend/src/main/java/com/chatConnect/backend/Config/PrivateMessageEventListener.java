package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.PrivateMessageCreatedEvent;
import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.ChatMessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component

public class PrivateMessageEventListener {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePrivateMessageCreated(PrivateMessageCreatedEvent privateMessageCreatedEvent){
        ChatMessage chatMessage=privateMessageCreatedEvent.getChatMessage();
        String status=privateMessageCreatedEvent.getStatus();
        ChatMessageResponseDTO chatMessageResponseDTO=new ChatMessageResponseDTO(chatMessage.getMsg_id(), chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),status);
        simpMessagingTemplate.convertAndSendToUser(chatMessageResponseDTO.getSenderName(),"/queue/private",chatMessageResponseDTO);
        simpMessagingTemplate.convertAndSendToUser(chatMessageResponseDTO.getReceiverName(),"/queue/private",chatMessageResponseDTO);


    }
}
