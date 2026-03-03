package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.PrivateMessageDeletedEvent;
import com.chatConnect.backend.Event.MessageDeletedForMeEvent;
import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.ChatMessageResponseDTO;
import com.chatConnect.backend.Modal.Users;
import com.chatConnect.backend.Repo.MessageDeliveryRepo;
import com.chatConnect.backend.Repo.MessageReadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component

public class PrivateMessageDeletedEventListener {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageDeliveryRepo messageDeliveryRepo;

    @Autowired
    private MessageReadRepo messageReadRepo;

    @EventListener

    public void notifyDeletion(PrivateMessageDeletedEvent privateMessageDeletedEvent){
        ChatMessage chatMessage=privateMessageDeletedEvent.getChatMessage();
        ChatMessageResponseDTO chatMessageResponseDTO=new ChatMessageResponseDTO(chatMessage.getMsg_id(), null,chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");

        Users receiver=chatMessage.getReceiver();
        boolean delivered=messageDeliveryRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
        if(delivered){
            chatMessageResponseDTO.setStatus("DELIVERED");
        }
        boolean read=messageReadRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
        if(read){
            chatMessageResponseDTO.setStatus("READ");
        }
        chatMessageResponseDTO.setDeletedForEveryone(true);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver().getUsername(),"/queue/delete",chatMessageResponseDTO);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/delete",chatMessageResponseDTO);

    }


}
