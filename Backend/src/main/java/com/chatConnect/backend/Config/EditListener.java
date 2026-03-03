package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.PrivateMessageEditedEvent;
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

public class EditListener {

    @Autowired

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageDeliveryRepo messageDeliveryRepo;

    @Autowired
    private MessageReadRepo messageReadRepo;

    @EventListener
    public void notifyPrivateMessageEdited(PrivateMessageEditedEvent privateMessageEditedEvent){
        ChatMessage chatMessage=privateMessageEditedEvent.getChatMessage();
        ChatMessageResponseDTO chatMessageResponseDTO=new ChatMessageResponseDTO(chatMessage.getMsg_id(), chatMessage.getContent(),chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");


        Users receiver=chatMessage.getReceiver();
        boolean delivered=messageDeliveryRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
        if(delivered){
            chatMessageResponseDTO.setStatus("DELIVERED");
        }
        boolean read=messageReadRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
        if(read){
            chatMessageResponseDTO.setStatus("READ");
        }
        if(chatMessage.isDeletedForEveryone()){
            chatMessageResponseDTO.setDeletedForEveryone(true);
        }
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/message/edit",chatMessageResponseDTO);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver().getUsername(),"/queue/message/edit",chatMessageResponseDTO);
    }
}
