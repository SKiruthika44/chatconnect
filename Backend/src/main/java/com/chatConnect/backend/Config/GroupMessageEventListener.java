package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.GroupMessageCreatedEvent;
import com.chatConnect.backend.Modal.GroupMessage;
import com.chatConnect.backend.Modal.GroupMessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component

public class GroupMessageEventListener {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessageAfterCommit(GroupMessageCreatedEvent groupMessageCreatedEvent){
        GroupMessage groupMessage=groupMessageCreatedEvent.getGroupMessage();
        GroupMessageResponseDTO responseDTO=new GroupMessageResponseDTO(groupMessage.getId(), groupMessage.getContent(), groupMessage.getCreatedAt(),"SENT",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());
        if(groupMessage.getDeliveredUsers().size()==groupMessage.getGroupChat().getGroupMembers().size()){
            responseDTO.setStatus("DELIVERED");
        }
        simpMessagingTemplate.convertAndSend("/topic/group/"+groupMessage.getGroupChat().getId(),responseDTO);
        System.out.println("message sent after commit"+groupMessage.getId());

    }
}
